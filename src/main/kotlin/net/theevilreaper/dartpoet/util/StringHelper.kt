package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.DartModifier

/**
 * Utility class for string operations which are used to generate code parts.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
internal object StringHelper {

    /**
     * Joins a given [Set] of [DartModifier] to a string representation.
     * @param modifiers the [Set] of [DartModifier] to join
     * @param prefix the prefix for the string
     * @param separator the separator for the string
     * @param postfix the postfix for the string
     * @param value the function to get the value from the [DartModifier]
     * @return the joined string or an empty string if the [Set] is empty
     */
    inline fun joinModifiers(
        modifiers: Set<DartModifier>,
        prefix: String = EMPTY_STRING,
        separator: String = EMPTY_STRING,
        postfix: String = EMPTY_STRING,
        crossinline value: (DartModifier) -> String = { it.identifier }
    ): String {
        if (modifiers.isEmpty()) return EMPTY_STRING
        return modifiers.joinToString(prefix = prefix, separator = separator, postfix = postfix) { value(it) }
    }

    /**
     * Ensures that the given variable name has the private modifier if the [withPrivate] parameter is true.
     * @param name the name of the variable
     * @param withPrivate the flag to check if the variable name should have the private modifier
     * @return the variable name with the private modifier or the name itself
     */
    fun ensureVariableNameWithPrivateModifier(name: String, withPrivate: Boolean): String {
        require(name.trim().isNotEmpty()) { "The name parameter can't be empty" }
        return when (withPrivate) {
            true -> "${DartModifier.PRIVATE.identifier}$name"
            false -> name
        }
    }
}