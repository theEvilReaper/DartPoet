package net.theevilreaper.dartpoet.extension.type

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * An [ExtensionTypeSpec] represents a Dart 3.3 extension type declaration.
 * It implements [TypeSpec] so it can be used anywhere a top-level type declaration is expected.
 *
 * An extension type wraps exactly one representation value without allocating a new object at
 * runtime. Unlike a class it can't declare instance fields - only static properties, methods,
 * getters, setters, and operators are allowed in its body.
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
class ExtensionTypeSpec internal constructor(
    builder: ExtensionTypeBuilder
) : TypeSpec {
    override val name: String = builder.name
    internal val representationParameter: ParameterSpec = builder.representationParameter
    internal val constructorName: String? = builder.constructorName
    internal val isConst: Boolean = builder.isConst
    internal val interfaces: List<TypeName> = builder.interfaces.toImmutableList()
    internal val genericCasts: Set<TypeName> = builder.genericCasts.toImmutableSet()
    internal val properties: Set<PropertySpec> = builder.properties.toImmutableSet()
    internal val functions: Set<FunctionSpec> = builder.functions.toImmutableSet()
    internal val operators: Set<DartOperatorSpec> = builder.operators.toImmutableSet()
    internal val constants: Set<ConstantPropertySpec> = builder.constants.toImmutableSet()
    internal val annotations: Set<AnnotationSpec> = builder.specData.annotations.toImmutableSet()
    internal val modifiers: Set<DartModifier> = builder.specData.modifiers.toImmutableSet()
    internal val endsWithNewLine: Boolean = builder.endWithNewLine

    internal val hasNoContent: Boolean
        get() = properties.isEmpty() && functions.isEmpty() && operators.isEmpty() && constants.isEmpty()

    init {
        check(name.isNotBlank() && !name.contains(" ")) { "The extension type name can not be empty or contain whitespaces" }

        if (constructorName != null) {
            check(constructorName.isNotBlank() && !constructorName.contains(" ")) {
                "The extension type constructor name can not be empty or contain whitespaces"
            }
        }

        val invalidModifiers = modifiers.filter { it != DartModifier.PUBLIC && it != DartModifier.PRIVATE }
        check(invalidModifiers.isEmpty()) {
            "An extension type can only have the PUBLIC or PRIVATE modifier, but got: $invalidModifiers"
        }

        check(representationParameter.typeName != null) {
            "The representation parameter of an extension type needs an explicit type"
        }
        check(representationParameter.type == ParameterType.POSITIONAL) {
            "The representation parameter of an extension type must be a simple positional parameter"
        }
        check(!representationParameter.coVariant) {
            "The representation parameter of an extension type can't be covariant"
        }
        check(!representationParameter.hasInitializer) {
            "The representation parameter of an extension type can't have a default value"
        }
        check(!representationParameter.isSuperParameter) {
            "The representation parameter of an extension type can't be a super parameter"
        }

        check(interfaces.size == interfaces.distinct().size) {
            "Duplicate interface type(s) found: ${interfaces.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }

        check(operators.size == operators.distinctBy { it.operator }.size) {
            "Duplicate operator(s) found: ${operators.groupingBy { it.operator }.eachCount().filterValues { it > 1 }.map { it.key.symbol }}"
        }

        check(properties.all { DartModifier.STATIC in it.modifiers }) {
            "An extension type can't have instance properties, only static properties are allowed"
        }
    }

    override fun write(codeWriter: CodeWriter) {
        WriterHelper.extensionTypeWriter.write(this, codeWriter)
    }

    override fun toString() = buildCodeString { write(this) }

    /**
     * Converts a [ExtensionTypeSpec] reference into a new [ExtensionTypeBuilder] instance.
     * @return the created [ExtensionTypeBuilder] instance
     */
    fun toBuilder(): ExtensionTypeBuilder {
        val builder = ExtensionTypeBuilder(name, representationParameter)
        builder.constructorName = constructorName
        builder.isConst = isConst
        builder.interfaces.addAll(interfaces)
        builder.genericCasts.addAll(genericCasts)
        builder.properties.addAll(properties)
        builder.functions.addAll(functions)
        builder.operators.addAll(operators)
        builder.constants.addAll(constants)
        builder.specData.annotations.addAll(annotations)
        builder.specData.modifiers.addAll(modifiers)
        builder.endWithNewLine = endsWithNewLine
        return builder
    }

    companion object {
        @JvmStatic
        fun builder(name: String, representationParameter: ParameterSpec) = ExtensionTypeBuilder(name, representationParameter)

        @JvmStatic
        fun builder(name: String, representationParameter: ParameterSpec, vararg modifiers: DartModifier) =
            ExtensionTypeBuilder(name, representationParameter, *modifiers)
    }
}
