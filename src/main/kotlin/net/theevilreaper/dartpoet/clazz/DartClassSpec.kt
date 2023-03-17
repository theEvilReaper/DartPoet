package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartClassType

class DartClassSpec internal constructor(
    builder: DartClassBuilder
) {

    internal val name = builder.name
    internal val classType = builder.classType





    companion object {

        /**
         * Create a new [DartClassBuilder] instance for a normal dart class.
         * @return the created instance
         */
        @JvmStatic fun builder(name: String): DartClassBuilder = DartClassBuilder(name, DartClassType.CLASS)

        /**
         * Create a new [DartClassBuilder] instance for an anonymous dart class.
         * @return the created instance
         */
        @JvmStatic fun anonymousClassBuilder(): DartClassBuilder = DartClassBuilder( null, DartClassType.CLASS)

        /**
         * Create a new [DartClassBuilder] instance for a enum dart class.
         * @return the created instance
         */
        @JvmStatic fun enumClass(name: String): DartClassBuilder = DartClassBuilder(name, DartClassType.ENUM)

        /**
         * Create a new [DartClassBuilder] instance for a mixin dart class.
         * @return the created instance
         */
        @JvmStatic fun mixinClass(name: String): DartClassBuilder = DartClassBuilder(name, DartClassType.MIXIN)
    }
}
