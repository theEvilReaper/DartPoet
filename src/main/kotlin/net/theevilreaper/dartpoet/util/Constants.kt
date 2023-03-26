package net.theevilreaper.dartpoet.util

import java.util.regex.Pattern

internal const val DEFAULT_INDENT = " "
internal const val EMPTY_STRING = ""
internal const val NULL_STRING = "null"
internal const val NEW_LINE = "\n"
internal const val SEMICOLON = ";"

internal const val AS_PART = "as"
internal const val IMPORT = "import"
internal const val ANNOTATION_CHAR = "@"
internal const val DART_FILE_ENDING = ".dart"
internal val NEW_LINE_SPLIT: Pattern = Pattern.compile(NEW_LINE)

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