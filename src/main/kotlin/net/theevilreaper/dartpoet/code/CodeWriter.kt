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
 * The file contains only this methods which are needed to write the code for Dart.
 * All main method which writes the code in KotlinPoet still exists in this writer adaption.
 * Some others has been removed because they are not required
 */
package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.stringLiteralWithQuotes
import java.io.Closeable

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
 * Converts a [DartFile] to a string suitable to both human- and kotlinc-consumption. This honors
 * imports, indentation, and deferred variable names.
 */
class CodeWriter(
    out: Appendable,
    private val indent: String = DEFAULT_INDENT,
    columnLimit: Int = 100,
) : Closeable {
    var out = LineWrapper(out, indent, columnLimit)
    private var indentLevel = 0
    private var comment = false
    private var trailingNewline = false

    /**
     * When emitting a statement, this is the line of the statement currently being written. The first
     * line of a statement is indented normally and subsequent wrapped lines are double-indented. This
     * is -1 when the currently-written line isn't part of a statement.
     */
    private var statementLine = -1

    /**
     * Increases the current indentation level by a given amount.
     * The default value is 1.
     * @param levels the amount of levels to increase
     * @return the instance from the writer
     */
    fun indent(levels: Int = 1) = apply {
        indentLevel += levels
    }

    /**
     * Decreases the current indentation level by a given amount.
     * The default value is 1.
     * @param levels the amount of levels to decrease
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
     * @return the instance from the writer
     * @param format the format to use
     * @param args the arguments to use
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
     * Emits an empty curly block.
     *
     * **Note:** This method will emit `()` without any more content.
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
                "%S", "%C" -> {
                    val string = codeBlock.args[a++] as String?
                    // Emit null as a literal null: no quotes.
                    val literal = if (string != null) {
                        stringLiteralWithQuotes(
                            string,
                            isInsideRawString = false,
                            isConstantContext = isConstantContext,
                        )
                    } else {
                        NULL_STRING
                    }

                    if (part == "%C") {
                        emit(literal.replace("\"", "'"), nonWrapping = true)
                    } else {
                        emit(literal, nonWrapping = true)
                    }
                }

                "%P" -> {
                    val string = codeBlock.args[a++]?.let { arg ->
                        if (arg is CodeBlock) {
                            arg.toString(this@CodeWriter)
                        } else {
                            arg as String?
                        }
                    }
                    // Emit null as a literal null: no quotes.
                    val literal = if (string != null) {
                        stringLiteralWithQuotes(
                            string,
                            isInsideRawString = true,
                            isConstantContext = isConstantContext,
                        )
                    } else {
                        NULL_STRING
                    }
                    //TODO: Add better fix later
                    val updated: String = "'" + literal.replace("\"", "") + "'"

                    emit(updated, nonWrapping = true)
                    // emit(literal.replace("\"", "'"), nonWrapping = true)
                }

                "%T" -> {
                    var typeName = codeBlock.args[a++] as TypeName
                    typeName.emit(this)
                }

                "%%" -> emit("%")
                "⇥" -> indent()
                "⇤" -> unindent()
                "«" -> {
                    CodeWriterCheck.ensureStatementNotAlreadyOpen(statementLine, codeBlock)
                    statementLine = 0
                }

                "»" -> {
                    CodeWriterCheck.ensureStatementIsOpenBeforeClosing(statementLine, codeBlock)
                    if (statementLine > 0) {
                        unindent(2) // End a multi-line statement. Decrease the indentation level.
                    }
                    statementLine = -1
                }

                else -> {
                    emit(part)
                }
            }
        }
        if (ensureTrailingNewline && out.hasPendingSegments) {
            emit(NEW_LINE)
        }
    }

    private fun emitLiteral(o: Any?, isConstantContext: Boolean) {
        when (o) {
            /*is TypeSpec -> o.emit(this, null)
            is AnnotationSpec -> o.emit(this, inline = true, asParameter = isConstantContext)
            is PropertySpec -> o.emit(this, emptySet())
            is FunSpec -> o.emit(
                codeWriter = this,
                enclosingName = null,
                implicitModifiers = setOf(KModifier.PUBLIC),
                includeKdocTags = true,
            )
            is TypeAliasSpec -> o.emit(this)*/
            is CodeBlock -> emitCode(o, isConstantContext = isConstantContext)
            else -> emit(o.toString())
        }
    }

    /**
     * Emits `s` with indentation as required. It's important that all code that writes to
     * [CodeWriter.out] does it through here, since we emit indentation lazily in order to avoid
     * unnecessary trailing whitespace.
     */
    fun emit(s: String, nonWrapping: Boolean = false) = apply {
        var first = true
        val lines: List<String> = s.split('\n')
        for (line in lines) {
            // Emit a newline character. Make sure blank lines in KDoc & comments look good.
            if (!first) {
                if (comment && trailingNewline) {
                    emitIndentation()
                    out.appendNonWrapping(DOCUMENTATION_CHAR)
                }
                out.newline()
                trailingNewline = true
                if (statementLine != -1) {
                    if (statementLine == 0) {
                        indent(2) // Begin multiple-line statement. Increase the indentation level.
                    }
                    statementLine++
                }
            }

            first = false
            if (line.isEmpty()) continue // Don't indent empty lines.

            // Emit indentation and comment prefix if necessary.
            if (trailingNewline) {
                emitIndentation()
                if (comment) {
                    // To get insides why we are writing /// for documentation
                    // Please take a look at this side https://dart.dev/effective-dart/documentation
                    out.appendNonWrapping("$DOCUMENTATION_CHAR ")

                }
            }

            if (nonWrapping) {
                out.appendNonWrapping(line)
            } else {
                out.append(
                    line,
                    indentLevel = indentLevel + 2,
                )
            }
            trailingNewline = false
        }
    }

    private fun emitIndentation() {
        for (j in 0 until indentLevel) {
            out.appendNonWrapping(indent)
        }
    }

    /**
     * Emits a specific amount of [SPACE] strings to the given [Appendable].
     * @param amount the amount of spaces which should be applied
     * @return the instance from the writer
     */
    fun emitSpaces(amount: Int = 1) = apply {
        check(amount > 0) { "The amount can't be negative" }
        for (i in 0 until amount) {
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
     * Perform emitting actions on the current [CodeWriter] using a custom [Appendable]. The
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
    override fun close() {
        out.close()
    }
}
