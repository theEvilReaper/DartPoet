package net.theevilreaper.dartpoet.function

/**
 * Constructors in the programming language Dart has a bit of different syntax than in other languages.
 * To reduce the boilerplate code in the project and increase the readability / usage of the library there is a mapping structure.
 * The [FactoryDelegation] is a simple enum which contains the different types of constructor delegations for factories.
 * To use the enum there are some function in specific classes which can be used to the delegation.
 *
 * **Note**: The delegation is only used for factory constructors and not the normal variant of a constructor.
 * There are ways to detect the delegation by analyzing the data but this has more effort which is hard to maintain.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
enum class FactoryDelegation(val delegation: String) {

    NONE(""),
    LAMBDA("=>"),
    REDIRECT("=");
}
