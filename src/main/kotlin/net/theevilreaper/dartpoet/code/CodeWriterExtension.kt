package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.NEW_LINE

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
