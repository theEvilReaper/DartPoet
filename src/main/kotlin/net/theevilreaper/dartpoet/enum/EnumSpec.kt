package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * An [EnumSpec] represents an enum declaration in Dart.
 * It implements [TypeSpec] so it can be used anywhere a top-level type declaration is expected.
 *
 * @author theEvilReaper
 * @since 2.4.0
 */
class EnumSpec internal constructor(
    builder: EnumBuilder
) : TypeSpec {
    override val name: String = builder.name
    val entries: List<EnumEntrySpec> = builder.entries.toImmutableList()
    val mixins: List<TypeName> = builder.mixins.toImmutableList()
    val interfaces: List<TypeName> = builder.interfaces.toImmutableList()
    val genericCasts: Set<TypeName> = builder.genericCasts.toImmutableSet()
    val properties: Set<PropertySpec> = builder.properties.toImmutableSet()
    val constructors: Set<ConstructorBase> = builder.constructors.toImmutableSet()
    val functions: Set<FunctionSpec> = builder.functions.toImmutableSet()
    val operators: Set<DartOperatorSpec> = builder.operators.toImmutableSet()
    val constants: Set<ConstantPropertySpec> = builder.constants.toImmutableSet()
    val annotations: Set<AnnotationSpec> = builder.classMetaData.annotations.toImmutableSet()
    val modifiers: Set<DartModifier> = builder.classMetaData.modifiers.toImmutableSet()
    val endsWithNewLine: Boolean = builder.endWithNewLine

    internal val hasNoContent: Boolean
        get() = properties.isEmpty() && constructors.isEmpty() && functions.isEmpty() && operators.isEmpty() && constants.isEmpty()

    init {
        check(name.isNotBlank() && !name.contains(" ")) { "The enum name can not be empty or contain whitespaces" }
        check(entries.isNotEmpty()) { "An enum requires at least one enum entry" }

        check(entries.size == entries.distinctBy { it.name }.size) {
            "Duplicate enum entry name(s) found: ${entries.groupingBy { it.name }.eachCount().filterValues { it > 1 }.keys}"
        }

        val invalidModifiers = modifiers.filter { it != DartModifier.PUBLIC && it != DartModifier.PRIVATE }
        check(invalidModifiers.isEmpty()) {
            "Enums only support PUBLIC or PRIVATE modifiers, but got: $invalidModifiers"
        }

        val propertiesSize = properties.size
        entries.forEach {
            if (propertiesSize > 0 || it.parameters.isNotEmpty()) {
                check(it.parameters.size == propertiesSize) {
                    "The entries from the enum property must have the same size"
                }
            }
        }

        check(mixins.size == mixins.distinct().size) {
            "Duplicate mixin type(s) found: ${mixins.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }

        check(interfaces.size == interfaces.distinct().size) {
            "Duplicate interface type(s) found: ${interfaces.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }

        check(operators.size == operators.distinctBy { it.operator }.size) {
            "Duplicate operator(s) found: ${operators.groupingBy { it.operator }.eachCount().filterValues { it > 1 }.map { it.key.symbol }}"
        }
    }

    override fun write(codeWriter: CodeWriter) {
        WriterHelper.enumWriter.write(this, codeWriter)
    }

    override fun toString(): String = buildCodeString { write(this) }

    fun toBuilder(): EnumBuilder {
        val builder = EnumBuilder(this.name)
        builder.entries.addAll(this.entries)
        builder.mixins.addAll(this.mixins)
        builder.interfaces.addAll(this.interfaces)
        builder.genericCasts.addAll(this.genericCasts)
        builder.properties.addAll(this.properties)
        builder.constructors.addAll(this.constructors)
        builder.functions.addAll(this.functions)
        builder.operators.addAll(this.operators)
        builder.constants.addAll(this.constants)
        builder.classMetaData.annotations.addAll(this.annotations)
        builder.classMetaData.modifiers.addAll(this.modifiers)
        builder.endWithNewLine = this.endsWithNewLine
        return builder
    }

    companion object {
        @JvmStatic
        fun builder(name: String) = EnumBuilder(name)

        @JvmStatic
        fun builder(name: String, vararg modifiers: DartModifier) = EnumBuilder(name, *modifiers)
    }
}
