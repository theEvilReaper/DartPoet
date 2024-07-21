package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.DartModifier

// The documentation from dart says that maximum length of a line is 80
internal const val MAX_LINE_LENGTH = 80
internal const val EMPTY_STRING = ""
internal const val NULL_STRING = "null"
internal const val NULLABLE_CHAR = "?"

internal const val SPACE_CHAR = ' '
internal const val SPACE = SPACE_CHAR.toString()
internal const val COMMA_SEPARATOR = ", "
const val DEFAULT_INDENT = "  "

internal const val NEW_LINE_CHAR = '\n'
internal const val NEW_LINE = NEW_LINE_CHAR.toString()
internal const val SEMICOLON = ";"
internal const val DOCUMENTATION_CHAR = "///"
internal const val ANNOTATION_CHAR = "@"
internal const val DART_FILE_ENDING = ".dart"

//Brackets
internal const val CURLY_OPEN = '{'
internal const val CURLY_CLOSE = '}'
internal const val ROUND_OPEN = "("
internal const val ROUND_CLOSE = ")"
internal const val LESS_THAN_SIGN = "<"
internal const val GREATER_THAN_SIGN = ">"

internal val ALLOWED_FUNCTION_MODIFIERS = setOf(DartModifier.PUBLIC, DartModifier.PRIVATE, DartModifier.STATIC, DartModifier.ABSTRACT)
internal val ALLOWED_PROPERTY_MODIFIERS =
    setOf(DartModifier.PRIVATE, DartModifier.FINAL, DartModifier.LATE, DartModifier.STATIC, DartModifier.CONST)
internal val ALLOWED_CLASS_CONST_MODIFIERS = setOf(DartModifier.CONST)
internal val ALLOWED_CONST_MODIFIERS = setOf(DartModifier.STATIC, DartModifier.CONST)

//RegEx
private val namePattern: Regex = Regex("^[a-z]+(?:_[a-z]+)*\$")
private val lowerCamelCase: Regex = Regex("[a-z]+[A-Z0-9]*[a-z0-9]*[A-Za-z0-9]*")
private val indentPattern: Regex = Regex(" +")

internal val ALLOWED_PRIMITIVE_TYPES = setOf("Short", "Int", "Long", "Float", "Double", "Char", "Boolean")

//Error message
internal const val NO_PARAMETER_TYPE = "Parameter must have a type"
internal const val NO_GENERIC_ON_LIBRARIES = "A library class can't have generic types"

/**
 * Checks if a given set of [DartModifier] matches with a given set which contains the allowed [DartModifier].
 * @param rawModifiers contains all modifiers from the context
 * @param allowedModifiers contains all modifiers which are allowed for context
 * @param context contains the context from where the method is called
 */
fun hasAllowedModifiers(rawModifiers: Set<DartModifier>, allowedModifiers: Set<DartModifier>, context: String) {
    rawModifiers.also {
        LinkedHashSet(it).apply {
            removeAll(allowedModifiers)
            require(isEmpty()) { "These modifiers $this are not allowed in a $context context. Allowed modifiers: $allowedModifiers" }
        }
    }
}

/**
 * Checks if a filename matches the DartFile conventions for file names (not class names!)
 * @param fileName the file name to check
 * @return true if the name matches otherwise false
 */
fun isDartConventionFileName(fileName: String): Boolean {
    if (fileName.trim().isEmpty()) return false
    if (fileName.contains(DART_FILE_ENDING)) {
        return fileName.replace(DART_FILE_ENDING, EMPTY_STRING).matches(namePattern)
    }
    return fileName.matches(namePattern)
}

/**
 * Return a [Boolean] if the given string has the lowerCamelCase format.
 * @param input the string to check
 * @return true when the string has the format otherwise false
 */
fun isInLowerCamelCase(input: String): Boolean {
    return testStringForPattern(input, lowerCamelCase)
}

/**
 * Checks if a given [String] can be used as an indent.
 * @param input the string to check
 * @return true when the string only contains spaces otherwise false
 */
fun isIndent(input: String): Boolean {
    return testStringForPattern(input, indentPattern)
}

/**
 * Add base method for all the method which tests a string for a pattern.
 * @param input the string to check
 * @param pattern the pattern for the check
 * @return true when the string matches with the pattern otherwise false
 */
private fun testStringForPattern(input: String, pattern: Regex): Boolean {
    return input.isNotEmpty() && input.matches(pattern)
}

fun formatLowerCamelCase(input: String): String {
    return input.replaceFirstChar { it.lowercase() }
}
