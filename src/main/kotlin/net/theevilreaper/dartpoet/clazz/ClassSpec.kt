package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.DartModifier.CLASS
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ClassWriter
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.util.EXCLUSIVE_CLASS_MODIFIERS

/**
 * A [ClassBuilder] describes the actual content of the class.
 * The content includes functions, typedefs, const values etc.
 * Partly some things are also only allowed to be set on certain classes.
 * @param builder the [ClassBuilder] instance to retrieve data from it
 * @since 1.0.0
 * @author theEvilReaper
 */
class ClassSpec internal constructor(
    builder: ClassBuilder,
) : TypeSpec {
    override val name: String = builder.name.orEmpty()
    internal val classType: ClassType = builder.classType
    internal val modifiers: Set<DartModifier> = builder.classMetaData.modifiers.toImmutableSet()
    internal val annotations: Set<AnnotationSpec> = builder.classMetaData.annotations.toImmutableSet()
    internal val endsWithNewLine: Boolean = builder.endWithNewLine
    internal val isEnum: Boolean = builder.classType == ClassType.ENUM
    internal val isAbstract: Boolean = builder.classType == ClassType.ABSTRACT
    internal val isMixin: Boolean = builder.classType == ClassType.MIXIN
    internal val isMixinClass: Boolean = builder.classMetaData.modifiers.contains(DartModifier.MIXIN) && (builder.classType == ClassType.CLASS || builder.classType == ClassType.ABSTRACT)
    internal val isAnonymous: Boolean = builder.name == null && builder.classType == ClassType.CLASS
    internal val superClass: TypeName? = builder.superClass
    internal val mixins: List<TypeName> = builder.mixins.toImmutableList()
    internal val interfaces: List<TypeName> = builder.interfaces.toImmutableList()
    internal val onTypes: List<TypeName> = builder.onTypes.toImmutableList()
    internal val typeDefs: List<AbstractTypeDef<*>> = builder.typedefs.toImmutableList()
    internal val functions: Set<FunctionSpec> = builder.functionStack.toImmutableSet()
    internal val operators: Set<DartOperatorSpec> = builder.operatorStack.toImmutableSet()
    internal val properties: Set<PropertySpec> = builder.propertyStack.toImmutableSet()
    internal val constructors: Set<ConstructorBase> = builder.constructorStack.toImmutableSet()
    internal val enumPropertyStack: List<EnumEntrySpec> = builder.enumPropertyStack.toImmutableList()
    internal val constantStack = builder.constantStack.toImmutableSet()
    internal val genericCasts: Set<TypeName> = builder.genericCasts.toImmutableSet()
    /**
     * Returns true when the class has no content to generate.
     */
    internal val hasNoContent: Boolean
        get() = functions.isEmpty() && properties.isEmpty() && constructors.isEmpty() && constantStack.isEmpty() && enumPropertyStack.isEmpty() && operators.isEmpty() && typeDefs.isEmpty()

    init {
        if (isEnum) {
            check(enumPropertyStack.isNotEmpty()) { "A enum requires at least one enum property" }

            val propertiesSize: Int = properties.size

            enumPropertyStack.forEach {
                check(it.parameters.size == propertiesSize) { "The entries from the enum property must have the same size" }
            }

            check(superClass == null) {
                "An enum can't extend a class in Dart, only 'with' and 'implements' are allowed"
            }
        }

        if (isMixin) {
            check(superClass == null) {
                "A mixin declaration can't extend a class in Dart"
            }
            check(mixins.isEmpty()) {
                "A mixin declaration can't use Dart's 'with' clause"
            }
            val invalidMixinModifiers = modifiers.intersect(setOf(DartModifier.INTERFACE, DartModifier.FINAL, DartModifier.SEALED))
            check(invalidMixinModifiers.isEmpty()) {
                "A mixin can only have the 'base' modifier, but got: $invalidMixinModifiers"
            }
        }

        if (isMixinClass) {
            check(superClass == null) {
                "A mixin class can't extend a class in Dart"
            }
            check(mixins.isEmpty()) {
                "A mixin class can't use Dart's 'with' clause"
            }
            val invalidMixinClassModifiers = modifiers.intersect(setOf(DartModifier.INTERFACE, DartModifier.FINAL, DartModifier.SEALED))
            check(invalidMixinClassModifiers.isEmpty()) {
                "A mixin class can only be combined with 'base' or 'abstract', but got: $invalidMixinClassModifiers"
            }
        }

        check(isMixin || onTypes.isEmpty()) {
            "Dart's 'on' clause is only allowed on a mixin declaration"
        }

        check(mixins.size == mixins.distinct().size) {
            "Duplicate mixin type(s) found: ${mixins.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }

        check(onTypes.size == onTypes.distinct().size) {
            "Duplicate 'on' type(s) found: ${onTypes.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }

        check(interfaces.size == interfaces.distinct().size) {
            "Duplicate interface type(s) found: ${interfaces.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}"
        }

        check(operators.size == operators.distinctBy { it.operator }.size) {
            "Duplicate operator(s) found: ${operators.groupingBy { it.operator }.eachCount().filterValues { it > 1 }.map { it.key.symbol }}"
        }

        check(!isAnonymous || operators.isEmpty()) {
            "An anonymous class can't declare operators"
        }

        if (classType == ClassType.CLASS || classType == ClassType.ABSTRACT) {
            val exclusiveModifiers = modifiers.intersect(EXCLUSIVE_CLASS_MODIFIERS)
            check(exclusiveModifiers.size <= 1) {
                "A class can only have one of these modifiers at the same time: $EXCLUSIVE_CLASS_MODIFIERS, but got: $exclusiveModifiers"
            }
            check(!(isAbstract && DartModifier.SEALED in modifiers)) {
                "A sealed class can't be combined with the abstract modifier because sealed classes are implicitly abstract"
            }
        }
    }

    /**
     * Calls the [ClassWriter] to write the data from the spec into code for dart
     * @param codeWriter the [CodeWriter] instance to apply the data
     */
    override fun write(codeWriter: CodeWriter) {
        WriterHelper.classWriter.write(this, codeWriter)
    }

    /**
     * Returns a [String] representation from the class spec.
     * @return the generated representation as string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Converts a [ClassSpec] reference into a new [ClassBuilder] instance.
     * This allows the modification of a given spec reference.
     * By default, the spec class can't be modified because it's immutable.
     * @return the created [ClassBuilder] instance
     */
    fun toBuilder(): ClassBuilder {
        val classBuilder = ClassBuilder(name, classType)
        classBuilder.endWithNewLine = endsWithNewLine
        classBuilder.classMetaData.modifiers.addAll(modifiers)
        classBuilder.classMetaData.annotations.addAll(annotations)
        // Apply enum values to the builder when the class is an enum
        if (isEnum) {
            classBuilder.enumPropertyStack.addAll(enumPropertyStack)
        }

        classBuilder.propertyStack.addAll(properties)
        classBuilder.functionStack.addAll(functions)
        classBuilder.operatorStack.addAll(operators)
        classBuilder.constructorStack.addAll(constructors)
        classBuilder.constantStack.addAll(constantStack)
        classBuilder.typedefs.addAll(typeDefs)
        classBuilder.superClass = superClass
        classBuilder.mixins.addAll(mixins)
        classBuilder.interfaces.addAll(interfaces)
        classBuilder.onTypes.addAll(onTypes)
        classBuilder.genericCasts.addAll(genericCasts)
        return classBuilder
    }

    /**
     * The class contains methods to create a new [ClassBuilder] instance for a specific class.
     */
    companion object {

        /**
         * Create a new [ClassBuilder] instance for a normal dart class.
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String) = ClassBuilder(name, ClassType.CLASS, CLASS)

        /**
         * Create a new [ClassBuilder] instance for an anonymous dart class.
         * @return the created instance
         */
        @JvmStatic
        fun anonymousClassBuilder() = ClassBuilder(null, ClassType.CLASS)

        /**
         * Create a new [ClassBuilder] instance for an enum dart class.
         * @return the created instance
         * @deprecated Use [net.theevilreaper.dartpoet.enum.EnumSpec.builder] instead.
         */
        @Deprecated(
            message = "Use EnumSpec.builder(name) instead",
            replaceWith = ReplaceWith("EnumSpec.builder(name)", "net.theevilreaper.dartpoet.enum.EnumSpec")
        )
        @JvmStatic
        fun enumClass(name: String) = ClassBuilder(name, ClassType.ENUM)

        /**
         * Create a new [ClassBuilder] instance for a mixin dart class.
         * @return the created instance
         * @deprecated Use [net.theevilreaper.dartpoet.mixin.MixinSpec.builder] instead.
         */
        @Deprecated(
            message = "Use MixinSpec.builder(name) instead",
            replaceWith = ReplaceWith("MixinSpec.builder(name)", "net.theevilreaper.dartpoet.mixin.MixinSpec")
        )
        @JvmStatic
        fun mixinClass(name: String) = ClassBuilder(name, ClassType.MIXIN)

        /**
         * Create a new [ClassBuilder] instance for an abstract dart class.
         * @return the created instance
         */
        @JvmStatic
        fun abstractClass(name: String) = ClassBuilder(name, ClassType.ABSTRACT)
    }
}
