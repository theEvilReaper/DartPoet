package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ClassWriter
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartClassSpec internal constructor(
    builder: DartClassBuilder
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

    internal val superClass = builder.superClass
    internal val inheritKeyWord = builder.inheritKeyWord
    internal val classModifiers = modifiers.filter { it != WITH }.toImmutableSet()
    internal val functions = builder.functionStack.filter { !it.isTypeDef }.toImmutableSet()
    internal val properties = builder.propertyStack.toImmutableSet()
    internal val constructors = builder.constructorStack.toImmutableSet()
    internal val typeDefStack = builder.functionStack.filter { it.isTypeDef }.toImmutableSet()
    internal val enumPropertyStack = builder.enumPropertyStack.toImmutableList()

    internal val hasNoContent: Boolean
        get() = functions.isEmpty() && properties.isEmpty() && constructors.isEmpty()

    init {
        if (name != null) {
            check(name.trim().isNotEmpty()) { "The name can't be empty"}
        }

        /*check(isEnum && !this.modifiers.containsAnyOf(ABSTRACT, MIXIN)) {
            "An enum class can't have [${ABSTRACT.identifier}, ${MIXIN.identifier} as modifiers"
        }

        check (isAbstract && !this.modifiers.containsAnyOf(MIXIN, ENUM)) {
            "An abstract class can't have [${ABSTRACT.identifier}, ${ENUM.identifier} as modifiers"
        }*/
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        ClassWriter().write(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this
        )
    }

    /**
     * The class contains methods to create a new [DartClassBuilder] instance for a specific class.
     */
    companion object {

        /**
         * Create a new [DartClassBuilder] instance for a normal dart class.
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String) = DartClassBuilder(name, ClassType.CLASS, DartModifier.CLASS)

        /**
         * Create a new [DartClassBuilder] instance for an anonymous dart class.
         * @return the created instance
         */
        @JvmStatic
        fun anonymousClassBuilder() = DartClassBuilder(null, ClassType.CLASS)

        /**
         * Create a new [DartClassBuilder] instance for an enum dart class.
         * @return the created instance
         */
        @JvmStatic
        fun enumClass(name: String) = DartClassBuilder(name, ClassType.ENUM)

        /**
         * Create a new [DartClassBuilder] instance for a mixin dart class.
         * @return the created instance
         */
        @JvmStatic
        fun mixinClass(name: String) = DartClassBuilder(name, ClassType.MIXIN)

        /**
         * Create a new [DartClassBuilder] instance for an abstract dart class.
         * @return the created instance
         */
        @JvmStatic
        fun abstractClass(name: String) = DartClassBuilder(name, ClassType.ABSTRACT)

        @JvmStatic
        fun libraryClass(name: String) = DartClassBuilder(name, ClassType.LIBRARY)
    }
}
