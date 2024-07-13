package net.theevilreaper.dartpoet.function

/**
 * The programming language Dart allows different syntaxes to define a method.
 * Similar to Kotlin, Dart allows the use of setters and getters for properties, and functions can exhibit this behavior in Dart.
 * This enum defines the different types of accessor methods.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
enum class MethodAccessorType(val keyword: String) {

    SETTER("set"),
    GETTER("get"),
}
