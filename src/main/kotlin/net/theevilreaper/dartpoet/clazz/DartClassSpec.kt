package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.util.toImmutableSet

class DartClassSpec internal constructor(
    builder: DartClassBuilder
) {

    internal val name = builder.name
    internal val classType = builder.classType
    internal val modifiers = builder.classMetaData.modifiers.toImmutableSet()

    init {
        if (DartModifier.PUBLIC in modifiers) {
        }
    }

    companion object {

        /**
         * Create a new [DartClassBuilder] instance for a normal dart class.
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.CLASS)

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
        fun enumClass(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.CLASS)

        /**
         * Create a new [DartClassBuilder] instance for a mixin dart class.
         * @return the created instance
         */
        @JvmStatic
        fun mixinClass(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.MIXIN)

        /**
         * Create a new [DartClassBuilder] instance for a abstract dart class.
         * @return the created instance
         */
        @JvmStatic
        fun interfaceClass(name: String): DartClassBuilder = DartClassBuilder(name, ClassType.ABSTRACT)
    }
}
