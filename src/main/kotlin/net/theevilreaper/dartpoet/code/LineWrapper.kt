/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * https://github.com/square/kotlinpoet/blob/master/kotlinpoet/src/main/java/com/squareup/kotlinpoet/LineWrapper.kt
 *
 * Additional changes:
 * - Outsource clear logic into a own method
 * - Use constant values for \n and other chars
 */
package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.NEW_LINE_CHAR
import net.theevilreaper.dartpoet.util.SPACE
import net.theevilreaper.dartpoet.util.SPACE_CHAR
import java.io.Closeable

/**
 * The class is the LineWrapper from kotlinpoet (see header from the class) with some additional changes.
 */
class LineWrapper(
    private val out: Appendable,
    private val indent: String,
    private val maxLineLength: Int
) : Closeable {

    private var closed = false

    /**
     * Segments of the current line to be joined by spaces or wraps. Never empty, but contains a lone
     * empty string if no data has been emitted since the last newline.
     */
    private val segments = mutableListOf("")

    /** Number of indents in wraps. -1 if the current line has no wraps. */
    private var indentLevel = -1

    /** Optional prefix that will be prepended to wrapped lines. */
    private var linePrefix = ""

    /** @return whether there are pending segments for the current line. */
    val hasPendingSegments get() = segments.size != 1 || segments[0].isNotEmpty()

    /** Emit `s` replacing its spaces with line wraps as necessary. */
    fun append(string: String, indentLevel: Int = -1, linePrefix: String = "") {
        check(!closed) { "closed" }

        var pos = 0
        while (pos < string.length) {
            when (string[pos]) {
                SPACE_CHAR -> {
                    // Each space starts a new empty segment.
                    this.indentLevel = indentLevel
                    this.linePrefix = linePrefix
                    segments += EMPTY_STRING
                    pos++
                }

                NEW_LINE_CHAR -> {
                    // Each newline emits the current segments.
                    newline()
                    pos++
                }

                '·' -> {
                    // Render · as a non-breaking space.
                    segments[segments.size - 1] += SPACE
                    pos++
                }

                else -> {
                    var next = string.indexOfAny(SPECIAL_CHARACTERS, pos)
                    if (next == -1) next = string.length
                    segments[segments.size - 1] += string.substring(pos, next)
                    pos = next
                }
            }
        }
    }

    /** Emit `s` leaving spaces as-is. */
    fun appendNonWrapping(s: String) {
        check(!closed) { "closed" }
        require(!s.contains(NEW_LINE))
        segments[segments.size - 1] += s
    }

    fun newline() {
        check(!closed) { "closed" }
        emitCurrentLine()
        out.appendLine()
        indentLevel = -1
    }

    /** Flush any outstanding text and forbid future writes to this line wrapper.  */
    override fun close() {
        emitCurrentLine()
        closed = true
    }

    /**
     * Writes the current line into the given [Appendable].
     */
    private fun emitCurrentLine() {
        foldUnsafeBreaks()

        var start = 0
        var columnCount = segments[0].length

        for (i in 1 until segments.size) {
            val segment = segments[i]
            val newColumnCount = columnCount + 1 + segment.length

            // If this segment doesn't fit in the current run, print the current run and start a new one.
            if (newColumnCount > maxLineLength) {
                emitSegmentRange(start, i)
                start = i
                columnCount = segment.length + indent.length * indentLevel
                continue
            }

            columnCount = newColumnCount
        }

        // Print the last run.
        emitSegmentRange(start, segments.size)
        clear()
    }

    private fun clear() {
        segments.clear()
        segments += EMPTY_STRING
    }

    private fun emitSegmentRange(startIndex: Int, endIndex: Int) {
        // If this is a wrapped line we need a newline and an indent.
        if (startIndex > 0) {
            out.appendLine()
            for (i in 0 until indentLevel) {
                out.append(indent)
            }
            out.append(linePrefix)
        }

        // Emit each segment separated by spaces.
        out.append(segments[startIndex])
        for (i in startIndex + 1 until endIndex) {
            out.append(SPACE)
            out.append(segments[i])
        }
    }

    /**
     * Any segment that starts with '+' or '-' can't have a break preceding it. Combine it with the
     * preceding segment. Note that this doesn't apply to the first segment.
     */
    private fun foldUnsafeBreaks() {
        var i = 1
        while (i < segments.size) {
            val segment = segments[i]
            if (UNSAFE_LINE_START.matches(segment)) {
                segments[i - 1] = segments[i - 1] + SPACE + segments[i]
                segments.removeAt(i)
                if (i > 1) i--
            } else {
                i++
            }
        }
    }
}