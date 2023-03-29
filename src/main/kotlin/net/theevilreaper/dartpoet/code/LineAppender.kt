package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SPACE
import java.io.Closeable

class LineAppender(
    private val out: Appendable,
    private val indent: String,
    private val maxLineLength: Int
): Closeable {

    private val stringParts = mutableListOf("")
    private var closed = false
    private var indentLevel = -1

    fun append(string: String, indentLevel: Int = -1) {
        check(!closed) { "Unable to use appender on a closed appendable" }
        var pos = 0;
        while (pos < string.length) {
            when(string[pos]) {
                '\n' -> {
                    // The \n tells that we need a new line. For that we're writing the current line and after that we append a new line
                    writeNewLine()
                    pos++
                }
                ' ' -> {
                    this.indentLevel = indentLevel
                    this.stringParts += EMPTY_STRING
                    pos++
                }
                else -> {
                    var next = string.indexOfAny(SPECIAL_CHARACTERS, pos)
                    if (next == -1) next = string.length
                    stringParts[stringParts.size - 1] += string.substring(pos, next)
                    pos = next
                }
            }

        }
    }

    fun writeNoWrapping(string: String) {
        require(!closed) { "Unable to write text on a closed appendable"}
        require(string.trim().isNotEmpty()) { "Unable to write an empty text" }
        require(!string.contains("\n"))
        this.stringParts[this.stringParts.size - 1] += string
    }

    private fun writeCurrentLine() {
        foldUnsafeBreaks()
        var pos = 0;
        var maxCount = stringParts[0].length

        for (i in 1 until stringParts.size) {
            val segment = stringParts[i]
            val newLineCount = maxCount + 1 + segment.length

            if (newLineCount > maxLineLength) {
                emitSegmentRange(pos, i)
                pos = i
                maxCount = segment.length + indent.length * indentLevel
                continue
            }
            maxCount = newLineCount
        }

        // Print the last run.
        emitSegmentRange(pos, stringParts.size)
        reset()
    }

    private fun reset() {
        this.stringParts.clear()
        this.stringParts += EMPTY_STRING
    }

    /**
     * The method is called when the string is longer than the allowed maximum length of a line.
     * @param startIndex the index to start from the string segment
     * @param endIndex the index to end from the string segment
     */
    private fun emitSegmentRange(startIndex: Int, endIndex: Int) {
        // If this is a wrapped line we need a newline and an indent.
        if (startIndex > 0) {
            out.append(NEW_LINE)
            for (i in 0 until indentLevel) {
                out.append(indent)
            }
        }

        // Emit each segment separated by spaces.
        out.append(stringParts[startIndex])
        for (i in startIndex + 1 until endIndex) {
            out.append(SPACE)
            out.append(stringParts[i])
        }
    }

    private fun writeNewLine() {
        require(!closed) { "Unable to use appender on a closed appendable" }
        writeCurrentLine()
        out.appendLine()
        indentLevel = -1
    }

    override fun close() {
        writeCurrentLine()
        closed = true;
    }

    private fun foldUnsafeBreaks() {
        var i = 1
        while (i < stringParts.size) {
            val segment = stringParts[i]
            if (UNSAFE_LINE_START.matches(segment)) {
                stringParts[i - 1] = stringParts[i - 1] + " " + stringParts[i]
                stringParts.removeAt(i)
                if (i > 1) i--
            } else {
                i++
            }
        }
    }
}