package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.ClassWriter
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartClassSpec internal constructor(
    builder: DartClassBuilder
) {

    internal val name = builder.name
    internal val classType = builder.classType
    internal val modifiers = builder.classMetaData.modifiers.toImmutableSet()
    internal val endsWithNewLine = builder.endWithNewLine
    internal val isEnum = builder.isEnumClass
    internal val isAbstract = builder.isAbstract
    internal val isMixin = builder.isMixinClass

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

    companion object {

        /**
         * Create a new [DartClassBuilder] instance for a normal dart class.
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.CLASS, DartModifier.CLASS)

        /**
         * Create a new [DartClassBuilder] instance for an anonymous dart class.
         * @return the created instance
         */
        @JvmStatic
        fun anonymousClassBuilder(): DartClassBuilder = DartClassBuilder(null, ClassType.CLASS)

        /**
         * Create a new [DartClassBuilder] instance for a enum dart class.
         * @return the created instance
         */
        @JvmStatic
        fun enumClass(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.CLASS, ENUM)

        /**
         * Create a new [DartClassBuilder] instance for a mixin dart class.
         * @return the created instance
         */
        @JvmStatic
        fun mixinClass(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.CLASS, MIXIN)

        /**
         * Create a new [DartClassBuilder] instance for a abstract dart class.
         * @return the created instance
         */
        @JvmStatic
        fun abstractClass(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.ABSTRACT, ABSTRACT, CLASS)
    }
}
