package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.DartModifier

// The documentation from dart says that maximum length of a line is 80
internal const val MAX_LINE_LENGTH = 80
internal const val EMPTY_STRING = ""
internal const val NULL_STRING = "null"

internal const val SPACE_CHAR = ' '
internal const val SPACE = SPACE_CHAR.toString()
const val DEFAULT_INDENT = "  "
internal const val NEW_LINE_CHAR = '\n'

internal const val NEW_LINE = NEW_LINE_CHAR.toString()

internal const val SEMICOLON = ";"
internal const val COMMENT = "//"
internal const val AS_PART = "as"
internal const val IMPORT = "import"
internal const val ANNOTATION_CHAR = "@"

internal const val DART_FILE_ENDING = ".dart"

//Brackets
internal const val CURLY_OPEN = '{'
internal const val CURLY_CLOSE = '}'

internal const val ROUND_OPEN = "("
internal const val ROUND_CLOSE = ")"

internal val ALLOWED_PARAMETER_MODIFIERS = setOf(DartModifier.PUBLIC, DartModifier.PRIVATE, DartModifier.LATE, DartModifier.FINAL, DartModifier.CONST, DartModifier.STATIC)
internal val ALLOWED_FUNCTION_MODIFIERS = setOf(DartModifier.PUBLIC, DartModifier.PRIVATE, DartModifier.STATIC, DartModifier.TYPEDEF)
internal val ALLOWED_PROPERTY_MODIFIERS = setOf(DartModifier.PRIVATE, DartModifier.FINAL, DartModifier.LATE, DartModifier.STATIC, DartModifier.CONST)
internal val ALLOWED_CLASS_CONST_MODIFIERS = setOf(DartModifier.STATIC, DartModifier.CONST)
internal val ALLOWED_CONST_MODIFIERS = setOf(DartModifier.CONST)
private val namePattern: Regex = Regex("[a-z]+|([a-z]+)_+([a-z]+)")
private val lowerCamelCase: Regex = Regex("[a-z]+[A-Z0-9]*[a-z0-9]*[A-Za-z0-9]*")

fun hasAllowedModifiers(rawModifiers: Set<DartModifier>, allowedModifiers: Set<DartModifier>, context: String) {
    rawModifiers.also {
        LinkedHashSet(it).apply {
            removeAll(allowedModifiers)
            if (!isEmpty()) {
                throw IllegalArgumentException("These modifiers $this are not allowed in a $context context. Allowed modifiers: $allowedModifiers")
            }
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
    if (input.trim().isEmpty()) return false
    return input.matches(lowerCamelCase)
}