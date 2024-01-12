package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ClassWriter
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * A [ClassBuilder] describes the actual content of the class.
 * The content includes functions, typedefs, const values etc.
 * Partly some things are also only allowed to be set on certain classes.
 * @since 1.0.0
 * @author theEvilReaper
 */
class ClassSpec internal constructor(
    builder: ClassBuilder
) {
    internal val name = builder.name
    internal val classType = builder.classType
    internal val modifiers = builder.classMetaData.modifiers.toImmutableSet()
    internal val annotations = builder.classMetaData.annotations.toImmutableSet()
    internal val endsWithNewLine = builder.endWithNewLine
    internal val isEnum = builder.isEnumClass
    internal val isAbstract = builder.isAbstract
    internal val isMixin = builder.isMixinClass
    internal val isAnonymous = builder.isAnonymousClass
    internal val isLibrary = builder.isLibrary

    internal val superClass = builder.superClass
    internal val inheritKeyWord = builder.inheritKeyWord
    internal val classModifiers = modifiers.filter { it != WITH }.toImmutableSet()
    internal val typeDefs = builder.typedefs.toImmutableList()
    internal val functions = builder.functionStack.toImmutableSet()
    internal val properties = builder.propertyStack.toImmutableSet()
    internal val constructors = builder.constructorStack.toImmutableSet()
    internal val enumPropertyStack = builder.enumPropertyStack.toImmutableList()
    internal var constantStack = builder.constantStack.toImmutableSet()

    /**
     * Returns true when the class has no content to generate.
     */
    internal val hasNoContent: Boolean
        get() = functions.isEmpty() && properties.isEmpty() && constructors.isEmpty() && constantStack.isEmpty() && enumPropertyStack.isEmpty()

    init {
        if (name != null) {
            check(name.trim().isNotEmpty()) { "The name can't be empty" }
        }

        /*check(isEnum && !this.modifiers.containsAnyOf(ABSTRACT, MIXIN)) {
            "An enum class can't have [${ABSTRACT.identifier}, ${MIXIN.identifier} as modifiers"
        }

        check (isAbstract && !this.modifiers.containsAnyOf(MIXIN, ENUM)) {
            "An abstract class can't have [${ABSTRACT.identifier}, ${ENUM.identifier} as modifiers"
        }*/
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
