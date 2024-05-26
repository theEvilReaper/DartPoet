package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.DartModifier.CLASS
import net.theevilreaper.dartpoet.DartModifier.WITH
import net.theevilreaper.dartpoet.InheritKeyword
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ClassWriter
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

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
) {
    internal val name: String = builder.name.orEmpty()
    internal val classType: ClassType = builder.classType
    internal val modifiers: Set<DartModifier> = builder.classMetaData.modifiers.toImmutableSet()
    internal val annotations: Set<AnnotationSpec> = builder.classMetaData.annotations.toImmutableSet()
    internal val endsWithNewLine: Boolean = builder.endWithNewLine
    internal val isEnum: Boolean = builder.isEnumClass
    internal val isAbstract: Boolean = builder.isAbstract
    internal val isMixin: Boolean = builder.isMixinClass
    internal val isAnonymous: Boolean = builder.isAnonymousClass
    internal val isLibrary: Boolean = builder.isLibrary
    internal val superClass: TypeName? = builder.superClass
    internal val inheritKeyWord: InheritKeyword? = builder.inheritKeyWord
    internal val classModifiers: Set<DartModifier> = modifiers.filter { it != WITH }.toImmutableSet()
    internal val typeDefs: List<TypeDefSpec> = builder.typedefs.toImmutableList()
    internal val functions: Set<FunctionSpec> = builder.functionStack.toImmutableSet()
    internal val properties: Set<PropertySpec> = builder.propertyStack.toImmutableSet()
    internal val constructors: Set<ConstructorBase> = builder.constructorStack.toImmutableSet()
    internal val enumPropertyStack: List<EnumPropertySpec> = builder.enumPropertyStack.toImmutableList()
    internal var constantStack = builder.constantStack.toImmutableSet()

    /**
     * Returns true when the class has no content to generate.
     */
    internal val hasNoContent: Boolean
        get() = functions.isEmpty() && properties.isEmpty() && constructors.isEmpty() && constantStack.isEmpty() && enumPropertyStack.isEmpty()

    init {
        if (!isLibrary) {
            check(name.isNotEmpty()) { "The name of a class can't be empty" }
        }

        if (isEnum) {
            check(enumPropertyStack.isNotEmpty()) { "A enum requires at least one enum property" }

            val propertiesSize: Int = properties.size

            enumPropertyStack.forEach {
                check(it.parameters.size == propertiesSize) { "The entries from the enum property must have the same size" }
            }
        }
    }

    /**
     * Calls the [ClassWriter] to write the data from the spec into code for dart
     * @param codeWriter the [CodeWriter] instance to apply the data
     */
    internal fun write(codeWriter: CodeWriter) {
        ClassWriter().write(this, codeWriter)
    }

    /**
     * Returns a [String] representation from the class spec.
     * @return the generated representation as string
     */
    override fun toString() = buildCodeString { write(this) }

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
         */
        @JvmStatic
        fun enumClass(name: String) = ClassBuilder(name, ClassType.ENUM)

        /**
         * Create a new [ClassBuilder] instance for a mixin dart class.
         * @return the created instance
         */
        @JvmStatic
        fun mixinClass(name: String) = ClassBuilder(name, ClassType.MIXIN)

        /**
         * Create a new [ClassBuilder] instance for an abstract dart class.
         * @return the created instance
         */
        @JvmStatic
        fun abstractClass(name: String) = ClassBuilder(name, ClassType.ABSTRACT)

        /**
         * Creates a new [ClassBuilder] instance for a library class.
         * @return the created instance
         */
        @JvmStatic
        fun libraryClass(name: String) = ClassBuilder(name, ClassType.LIBRARY)
    }
}
