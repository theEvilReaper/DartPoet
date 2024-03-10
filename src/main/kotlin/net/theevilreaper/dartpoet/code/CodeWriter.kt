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

import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.escapeCharacterLiterals
import net.theevilreaper.dartpoet.util.stringLiteralWithQuotes
import java.io.Closeable

internal inline fun buildCodeString(builderAction: CodeWriter.() -> Unit): String {
    val stringBuilder = StringBuilder()
    CodeWriter(stringBuilder, columnLimit = Integer.MAX_VALUE).use {
        it.builderAction()
    }
    return stringBuilder.toString()
}

internal fun buildCodeString(
    codeWriter: CodeWriter,
    builderAction: CodeWriter.() -> Unit,
): String {
    val stringBuilder = StringBuilder()
    codeWriter.emitInto(stringBuilder, builderAction)
    return stringBuilder.toString()
}

/**
 * Converts a [FileSpec] to a string suitable to both human- and kotlinc-consumption. This honors
 * imports, indentation, and deferred variable names.
 */
class CodeWriter constructor(
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

    fun indent(levels: Int = 1) = apply {
        indentLevel += levels
    }

    fun unindent(levels: Int = 1) = apply {
        require(indentLevel - levels >= 0) { "cannot unindent $levels from $indentLevel" }
        indentLevel -= levels
    }

    fun emitDoc(codeBlock: CodeBlock) {
        trailingNewline = true // Force the '///' prefix for the documentation.
        comment = true
        try {
            emitCode(codeBlock)
            emit(NEW_LINE)
        } finally {
            comment = false
        }
    }

    fun emitCode(s: String) = emitCode(CodeBlock.of(s))

    fun emitCode(format: String, vararg args: Any?) = emitCode(CodeBlock.of(format, *args))

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
                    emit(literal.replace("\"", "'"), nonWrapping = true)
                }
                "%T" -> {
                    var typeName = codeBlock.args[a++] as TypeName
                    typeName.emit(this)
                }
                "%%" -> emit("%")
                "⇥" -> indent()
                "⇤" -> unindent()
                "«" -> {
                    check(statementLine == -1) {
                        """
            |Can't open a new statement until the current statement is closed (opening « followed
            |by another « without a closing »).
            |Current code block:
            |- Format parts: ${codeBlock.formatParts.map(::escapeCharacterLiterals)}
            |- Arguments: ${codeBlock.args}
            |
            """.trimMargin()
                    }
                    statementLine = 0
                }

                "»" -> {
                    check(statementLine != -1) {
                        """
            |Can't close a statement that hasn't been opened (closing » is not preceded by an
            |opening «).
            |Current code block:
            |- Format parts: ${codeBlock.formatParts.map(::escapeCharacterLiterals)}
            |- Arguments: ${codeBlock.args}
            |
            """.trimMargin()
                    }
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
        for (line in s.split('\n')) {
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
