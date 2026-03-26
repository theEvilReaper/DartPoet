/*
 * Copyright (C) 2015 Square, Inc.
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
 * Changes to the file compared to the original:
 *
 * The file contains only the methods which are needed to write code for Dart.
 * All main methods which write code in KotlinPoet still exist in this writer adaption.
 * Some others have been removed because they are not required.
 */
package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.NEW_LINE

/**
 * Converts a given [CodeBlock] into a string representation.
 * @param builderAction the action to perform on the writer
 * @return the block as string
 */
internal inline fun buildCodeString(builderAction: CodeWriter.() -> Unit): String {
    val stringBuilder = StringBuilder()
    CodeWriter(stringBuilder, columnLimit = Integer.MAX_VALUE).use {
        it.builderAction()
    }
    return stringBuilder.toString()
}

/**
 * Converts a given [CodeBlock] into a string representation.
 * @param codeWriter the writer to write the code to
 * @param builderAction the action to perform on the writer
 * @return the block as string
 */
internal fun buildCodeString(
    codeWriter: CodeWriter,
    builderAction: CodeWriter.() -> Unit,
): String {
    val stringBuilder = StringBuilder()
    codeWriter.emitInto(stringBuilder, builderAction)
    return stringBuilder.toString()
}

internal val emptyRoundBlock = buildCodeString {
    emitCode("%L", ROUND_OPEN)
    emitCode("%L", ROUND_CLOSE)
}

internal val emptyCurlyBlock = buildCodeString {
    emitCode("%L", CURLY_OPEN)
    emitCode("%L", CURLY_CLOSE)
}

/**
 * Converts a [DartFile] to a string suitable for both human- and kotlinc-consumption. This honors
 * imports, indentation, and deferred variable names.
 */
class CodeWriter(
    out: Appendable,
    private val indent: String = DEFAULT_INDENT,
    columnLimit: Int = 100,
) : AutoCloseable {

    var out = LineWrapper(out, indent, columnLimit)
    private var indentLevel = 0
    private var comment = false
    private var trailingNewline = false

    /**
     * When emitting a statement, this is the line of the statement currently being written. The first
     * line of a statement is indented normally and later wrapped lines are double-indented.
     * This is -1 when the currently written line isn't part of a statement.
     */
    private var statementLine = -1

    /**
     * Increases the current indentation level by a given amount.
     * The default value is 1.
     * @param levels the number of levels to increase
     * @return the instance from the writer
     */
    fun indent(levels: Int = 1) = apply {
        indentLevel += levels
    }

    /**
     * Decreases the current indentation level by a given amount.
     * The default value is 1.
     * @param levels the number of levels to decrease
     * @return the instance from the writer
     */
    fun unindent(levels: Int = 1) = apply {
        require(indentLevel - levels >= 0) { "cannot unindent $levels from $indentLevel" }
        indentLevel -= levels
    }

    /**
     * Emits a [CodeBlock] which contains documentation data to the given [Appendable].
     * @return the instance from the writer
     */
    fun emitDoc(codeBlock: CodeBlock) = apply {
        trailingNewline = true // Force the '///' prefix for the documentation.
        comment = true
        try {
            emitCode(codeBlock)
            emit(NEW_LINE)
        } finally {
            comment = false
        }
    }

    /**
     * Emits a [String] which contains data to the given [Appendable].
     * The method wraps the given [String] into a [CodeBlock] and emits it.
     * @return the instance from the writer
     */
    fun emitCode(s: String) = emitCode(CodeBlock.of(s))

    /**
     * Emits a specific format with the given arguments to the [Appendable].
     * The method wraps the given format and arguments into a [CodeBlock] and emits it.
     * @param format the format to use
     * @param args the arguments to use
     * @return the instance from the writer
     */
    fun emitCode(format: String, vararg args: Any?) = emitCode(CodeBlock.of(format, *args))

    /**
     * Emits an empty block which contains round brackets.
     *
     * **Note:** This method will emit `()` without any more content.
     */
    fun emitEmptyRoundBrackets() {
        emitCode(emptyRoundBlock)
    }

    /**
     * Emits an empty block which contains curly brackets.
     *
     * **Note:** This method will emit `{}` without any more content.
     */
    fun emitEmptyCurlyBrackets() {
        emitCode(emptyCurlyBlock)
    }

    /**
     * Emits a generic block with the given format and arguments.
     * The method will emit the given format with the arguments wrapped in `<>`.
     * @param format the format to use
     * @param args the arguments to use
     * @return the instance from the writer
     */
    fun emitGenericBlock(format: String, vararg args: Any?) = apply {
        if (format.isEmpty() && args.isEmpty()) {
            emitCode("<>")
        } else {
            emitCode("<")
            emitCode(format, *args)
            emitCode(">")
        }
    }

    /**
     * Emits the given [codeBlock] to the underlying [Appendable].
     *
     * Supports the following format placeholders:
     * - `%L` — emits the argument as a literal value
     * - `%S` — emits the argument as a Dart string literal with double quotes (`"`), escaping `$`
     * - `%C` — emits the argument as a Dart string literal with single quotes (`'`), escaping `$`
     * - `%P` — emits the argument as a Dart string literal with single quotes (`'`), keeping `$` for string interpolation
     * - `%T` — emits the argument as a [TypeName]
     * - `%N` — emits the argument as a plain name (no escaping)
     * - `%%` — emits a literal `%`
     * - `⇥` — increases the indentation level
     * - `⇤` — decreases the indentation level
     * - `«` — opens a statement
     * - `»` — closes a statement
     *
     * @param codeBlock the [CodeBlock] to emit
     * @param isConstantContext whether the code is emitted in a constant context. Defaults to `false`
     * @param ensureTrailingNewline whether to append a newline if there are pending segments. Defaults to `false`
     */
    fun emitCode(
        codeBlock: CodeBlock,
        isConstantContext: Boolean = false,
        ensureTrailingNewline: Boolean = false,
    ) = apply {
        var a = 0
        val partIterator = codeBlock.formatParts.listIterator()
        while (partIterator.hasNext()) {
            when (val part = partIterator.next()) {
                "%L" -> emitLiteral(codeBlock.args[a++], isConstantContext)
                "%S" -> emitStringArg(codeBlock.args[a++] as String?, quoteChar = '"')
                "%C" -> emitStringArg(codeBlock.args[a++] as String?)
                "%P" -> {
                    val string = codeBlock.args[a++]?.let { arg ->
                        if (arg is CodeBlock) arg.toString(this@CodeWriter) else arg as String?
                    }
                    emitStringArg(string, escapeDollar = false)
                }

                "%T" -> (codeBlock.args[a++] as TypeName).emit(this)
                "%N" -> emit(codeBlock.args[a++] as String)
                "%%" -> emit("%")
                "⇥" -> indent()
                "⇤" -> unindent()
                "«" -> openStatement(codeBlock)
                "»" -> closeStatement(codeBlock)
                else -> emit(part)
            }
        }
        if (ensureTrailingNewline && out.hasPendingSegments) emit(NEW_LINE)
    }

    /**
     * Emits a Dart string literal for the given [string] argument.
     *
     * If [string] is `null`, emits [NULL_STRING] instead.
     *
     * @param string the string value to emit, or `null`
     * @param quoteChar the quote character to use. Defaults to `'`
     * @param escapeDollar whether to escape `$` signs. Set to `false` for string interpolation. Defaults to `true`
     */
    private fun emitStringArg(
        string: String?,
        quoteChar: Char = '\'',
        escapeDollar: Boolean = true,
    ) {
        val stringArgument = when (string != null) {
            true -> dartStringLiteral(string, quoteChar, escapeDollar)
            false -> NULL_STRING
        }
        emit(stringArgument, nonWrapping = true)
    }

    /**
     * Emits a literal value to the underlying [Appendable].
     *
     * If [o] is a [CodeBlock], it is emitted recursively via [emitCode].
     * Otherwise, the value is converted to a string via [toString] and emitted as-is.
     *
     * @param o the value to emit, or `null`
     * @param isConstantContext whether the value is emitted in a constant context
     */
    private fun emitLiteral(o: Any?, isConstantContext: Boolean) {
        when (o) {
            is CodeBlock -> emitCode(o, isConstantContext = isConstantContext)
            else -> emit(o.toString())
        }
    }

    /**
     * Opens a new statement in the current code block.
     *
     * Ensures that no statement is already open before proceeding.
     * Sets the internal statement line counter to `0` to mark the beginning of a statement.
     *
     * @param codeBlock the [CodeBlock] currently being emitted, used for error reporting
     * @throws IllegalStateException if a statement is already open
     */
    private fun openStatement(codeBlock: CodeBlock) {
        CodeWriterCheck.ensureStatementNotAlreadyOpen(statementLine, codeBlock)
        statementLine = 0
    }

    /**
     * Closes the currently open statement in the current code block.
     *
     * If the statement spans multiple lines, the indentation level is decreased by 2.
     * Resets the internal statement line counter to `-1` to mark the end of a statement.
     *
     * @param codeBlock the [CodeBlock] currently being emitted, used for error reporting
     * @throws IllegalStateException if no statement is currently open
     */
    private fun closeStatement(codeBlock: CodeBlock) {
        CodeWriterCheck.ensureStatementIsOpenBeforeClosing(statementLine, codeBlock)
        if (statementLine > 0) unindent(2)
        statementLine = -1
    }

    /**
     * Emits `s` with indentation as required. It's important that all code that writes to
     * [CodeWriter.out] does it through here, since we emit indentation lazily in order to avoid
     * unnecessary trailing whitespace.
     *
     * @param s the string to emit
     * @param nonWrapping whether to emit the string without line wrapping. Defaults to `false`
     */
    fun emit(s: String, nonWrapping: Boolean = false) = apply {
        var first = true
        for (line in s.split('\n')) {
            if (!first) emitNewline()
            first = false
            if (line.isEmpty()) continue
            emitLine(line, nonWrapping)
        }
    }

    /**
     * Emits a newline to the underlying [Appendable].
     *
     * If the current line is part of a comment and has a trailing newline, the indentation
     * and the [DOCUMENTATION_CHAR] are emitted before the newline.
     *
     * If a statement is currently open, the indentation level is increased by 2 when the
     * first newline of the statement is emitted.
     */
    private fun emitNewline() {
        if (comment && trailingNewline) {
            emitIndentation()
            out.appendNonWrapping(DOCUMENTATION_CHAR)
        }
        out.newline()
        trailingNewline = true
        if (statementLine != -1) {
            if (statementLine == 0) indent(2)
            statementLine++
        }
    }

    /**
     * Emits a single line to the underlying [Appendable].
     *
     * If [trailingNewline] is `true`, the indentation is emitted before the line.
     * When inside a comment block, the [DOCUMENTATION_CHAR] prefix is also prepended.
     *
     * If [nonWrapping] is `true`, the line is appended without any line wrapping.
     * Otherwise, the line is appended with the current indentation level taken into account.
     *
     * @param line the line to emit
     * @param nonWrapping whether to emit the line without line wrapping
     */
    private fun emitLine(line: String, nonWrapping: Boolean) {
        if (trailingNewline) {
            emitIndentation()
            if (comment) {
                // Dart documentation comments use '///' — see https://dart.dev/effective-dart/documentation
                out.appendNonWrapping("$DOCUMENTATION_CHAR ")
            }
        }
        if (nonWrapping) {
            out.appendNonWrapping(line)
        } else {
            out.append(line, indentLevel = indentLevel + 2)
        }
        trailingNewline = false
    }

    /**
     * Emits the current indentation level.
     */
    private fun emitIndentation() {
        repeat(indentLevel) {
            out.appendNonWrapping(indent)
        }
    }

    /**
     * Emits a specific amount of [SPACE] strings to the given [Appendable].
     * @param amount the number of spaces which should be applied
     * @return the instance from the writer
     */
    fun emitSpaces(amount: Int = 1) = apply {
        check(amount > 0) { "The amount can't be negative" }
        repeat(amount) {
            out.appendNonWrapping(SPACE)
        }
    }

    /**
     * Applies one [SPACE] string to the given [Appendable].
     * @return the instance from the writer
     */
    fun emitSpace() = apply {
        out.appendNonWrapping(SPACE)
    }

    /**
     * Performs emitting actions on the current [CodeWriter] using a custom [Appendable]. The
     * [CodeWriter] will continue using the old [Appendable] after this method returns.
     */
    inline fun emitInto(out: Appendable, action: CodeWriter.() -> Unit) {
        val codeWrapper = this
        LineWrapper(out, indent = DEFAULT_INDENT, maxLineLength = Int.MAX_VALUE).use { newOut ->
            val oldOut = codeWrapper.out
            codeWrapper.out = newOut
            action()
            codeWrapper.out = oldOut
        }
    }

    /**
     * Closes the underlying [Appendable].
     */
    override fun close() = out.close()
}