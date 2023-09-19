package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.DartModifier

/**
 * [ConstClassName] represents the `const` keyword from the programming language dart.
 * It can be used in each spec implementation which allows the usage of [TypeName]'s.
 *
 * @constructor Creates an instance of [ConstClassName]
 * @author theEvilReaper
 * @since 1.0.0
 */
internal class ConstClassName : ClassName(DartModifier.CONST.identifier) {

    /**
     * This method is overridden from the superclass and throws an [IllegalAccessException] with a message
     * indicating that the 'const' type cannot be copied.
     *
     * @param nullable Indicates whether the type is nullable
     * @throws IllegalAccessException Always throws an exception indicating that the 'dynamic' type cannot be copied
     */
    @Throws(IllegalAccessException::class)
    override fun copy(nullable: Boolean): TypeName {
        throw IllegalAccessException("The const type can't be copied")
    }
}
