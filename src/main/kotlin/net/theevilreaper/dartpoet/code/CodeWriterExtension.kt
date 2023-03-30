package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.NEW_LINE

internal val NO_ARG_PLACEHOLDERS = arrayOf('⇥', '⇤', '«', '»').toCharArray()
internal val SPECIAL_CHARACTERS = " \n·".toCharArray()
internal val UNSAFE_LINE_START = Regex("\\s*[-+].*")

fun String.withOpenBrackets(): String {

    for (i in length - 1 downTo 0) {
        if (this[i] == CURLY_OPEN) {
            return "$this$NEW_LINE"
        } else if (this[i] == CURLY_CLOSE) {
            break
        }
    }
    return "$this $CURLY_OPEN"
}

internal val Char.isSingleCharNoArgPlaceholder get() = this in NO_ARG_PLACEHOLDERS
internal val Char.isMultiCharNoArgPlaceholder get() = this == '%'

internal fun String.nextPotentialPlaceholderPosition(startIndex: Int) =
    indexOfAny(NO_ARG_PLACEHOLDERS, startIndex)
