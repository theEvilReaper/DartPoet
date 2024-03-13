package net.theevilreaper.dartpoet.function

import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec

/**
 * The [ConstructorDelegation] enum class provides the different types of constructor delegations which are available for [ConstructorSpec].
 * The delegation is used to reduce the boilerplate code and increase the readability of the library.
 * @param delegation the delegation to use
 * @since 1.0.0
 * @version 1.0.0
 */
enum class ConstructorDelegation(val delegation: String) {

    NONE(""),
    LAMBDA("=>"),
    INHERIT(":")
}