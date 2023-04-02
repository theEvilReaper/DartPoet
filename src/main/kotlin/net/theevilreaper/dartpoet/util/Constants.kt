package net.theevilreaper.dartpoet.util

import java.util.regex.Pattern

// The documentation from dart says that maximum length of a line is 80
internal const val MAX_LINE_LENGTH = 80
internal const val EMPTY_STRING = ""
internal const val NULL_STRING = "null"

internal const val SPACE_CHAR = ' '
internal const val SPACE = SPACE_CHAR.toString()
const val DEFAULT_INDENT = SPACE
internal const val NEW_LINE_CHAR = '\n'

internal const val NEW_LINE = NEW_LINE_CHAR.toString()

internal const val SEMICOLON = ";"
internal const val COMMENT = "//"
internal const val AS_PART = "as"
internal const val IMPORT = "import"
internal const val ANNOTATION_CHAR = "@"

internal const val DART_FILE_ENDING = ".dart"
internal val NEW_LINE_SPLIT: Pattern = Pattern.compile(NEW_LINE)

//Brackets
internal const val CURLY_OPEN = '{'
internal const val CURLY_CLOSE = '}'

private val namePattern: Pattern = Pattern.compile("([a-z]+)_+([a-z]+)")

/**
 * Checks if a filename matches the DartFile conventions for file names (not class names!)
 * @param fileName the file name to check
 * @return true if the name matches otherwise false
 */
fun isDartConventionFileName(fileName: String): Boolean {
    if (fileName.trim().isEmpty()) return false
    if (fileName.contains(DART_FILE_ENDING)) {
        return namePattern.matcher(fileName.replace(DART_FILE_ENDING, EMPTY_STRING)).matches()
    }
    return namePattern.matcher(fileName).matches()
}