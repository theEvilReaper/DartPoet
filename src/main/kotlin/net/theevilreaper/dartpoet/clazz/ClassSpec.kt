package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.DartModifier.CLASS
import net.theevilreaper.dartpoet.DartModifier.WITH
import net.theevilreaper.dartpoet.InheritKeyword
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ClassWriter
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
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
    internal val isEnum: Boolean = builder.classType == ClassType.ENUM
    internal val isAbstract: Boolean = builder.classType == ClassType.ABSTRACT
    internal val isMixin: Boolean = builder.classType == ClassType.MIXIN
    internal val isAnonymous: Boolean = builder.name == null && builder.classType == ClassType.CLASS
    internal val isLibrary: Boolean = builder.classType == ClassType.LIBRARY
    internal val superClass: TypeName? = builder.superClass
    internal val inheritKeyWord: InheritKeyword? = builder.inheritKeyWord
    internal val classModifiers: Set<DartModifier> = modifiers.filter { it != WITH }.toImmutableSet()
    internal val typeDefs: List<TypeDefSpec> = builder.typedefs.toImmutableList()
    internal val functions: Set<FunctionSpec> = builder.functionStack.toImmutableSet()
    internal val properties: Set<PropertySpec> = builder.propertyStack.toImmutableSet()
    internal val constructors: Set<ConstructorBase> = builder.constructorStack.toImmutableSet()
    internal val enumPropertyStack: List<EnumEntrySpec> = builder.enumPropertyStack.toImmutableList()
    internal val constantStack = builder.constantStack.toImmutableSet()
    internal val genericCasts: Set<TypeName> = builder.genericCasts.toImmutableSet()
    /**
     * Returns true when the class has no content to generate.
     */
    internal val hasNoContent: Boolean
        get() = functions.isEmpty() && properties.isEmpty() && constructors.isEmpty() && constantStack.isEmpty() && enumPropertyStack.isEmpty()

    init {
        if (isLibrary) {
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
        classBuilder.constructorStack.addAll(constructors)
        classBuilder.constantStack.addAll(constantStack)
        classBuilder.typedefs.addAll(typeDefs)
        classBuilder.superClass = superClass
        classBuilder.inheritKeyWord = inheritKeyWord
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
