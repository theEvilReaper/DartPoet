package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.DartModifier

/**
 * [DynamicClassName] extends [ClassName] and is used to represent the 'dynamic' type in Dart programming language.
 * It provides a mechanism to handle the 'dynamic' type, which is not typically used for copying operations.
 *
 * @constructor Creates an instance of [DynamicClassName].
 */
class DynamicClassName : ClassName(DartModifier.DYNAMIC.identifier) {

    /**
     * This method is overridden from the superclass and throws an [IllegalAccessException] with a message
     * indicating that the 'dynamic' type cannot be copied.
     *
     * @param nullable Indicates whether the type is nullable.
     * @throws IllegalAccessException Always throws an exception indicating that the 'dynamic' type cannot be copied.
     */
    @Throws(IllegalAccessException::class)
    override fun copy(nullable: Boolean): TypeName {
        throw IllegalAccessException("The dynamic type can't be copied")
    }
}