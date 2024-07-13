package net.theevilreaper.dartpoet.function

/**
 * The [FunctionDelegation] enum define some entries which are used to delegate the layout generation of a function.
 * @param identifier the identifier for the delegation
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
enum class FunctionDelegation(val identifier: String) {

    NONE(""),
    SHORTEN("=>"),

}