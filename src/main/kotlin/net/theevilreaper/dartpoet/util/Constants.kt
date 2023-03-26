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

internal val NEW_LINE_SPLIT: Pattern = Pattern.compile(NEW_LINE)
