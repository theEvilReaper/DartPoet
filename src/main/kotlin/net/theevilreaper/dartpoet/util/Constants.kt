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
internal const val CONSTRUCTOR = "CONSTRUCTOR"

//Brackets
internal const val CURLY_OPEN = '{'
internal const val CURLY_CLOSE = '}'

internal val ALLOWED_PARAMETER_MODIFIERS = setOf(DartModifier.PUBLIC, DartModifier.PRIVATE, DartModifier.LATE, DartModifier.CONST, DartModifier.STATIC)

private val namePattern: Regex = Regex("[a-z]+|([a-z]+)_+([a-z]+)")
private val lowerCamelCase: Regex = Regex("[a-z]+[A-Z0-9]*[a-z0-9]*[A-Za-z0-9]*")

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