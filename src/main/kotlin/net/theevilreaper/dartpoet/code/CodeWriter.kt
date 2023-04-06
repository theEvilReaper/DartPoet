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
 */
package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.import.DartImport
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import net.theevilreaper.dartpoet.util.escapeCharacterLiterals
import net.theevilreaper.dartpoet.util.stringLiteralWithQuotes
import net.theevilreaper.dartpoet.util.toEnumSet
import java.io.Closeable

/** Sentinel value that indicates that no user-provided package has been set.  */
private val NO_PACKAGE = String()

private fun extractMemberName(part: String): String {
    require(Character.isJavaIdentifierStart(part[0])) { "not an identifier: $part" }
    for (i in 1..part.length) {
        if (!part.substring(0, i).isPlaceholder) {
            return part.substring(0, i - 1)
        }
    }
    return part
}

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
    imports: Map<String, DartImport> = emptyMap(),
    columnLimit: Int = 100,
) : Closeable {
    var out = LineWrapper(out, indent, columnLimit)
    private var indentLevel = 0

    private var kdoc = false
    private var comment = false
    private var packageName = NO_PACKAGE
    private val typeSpecStack = mutableListOf<DartClassSpec>()
    private val memberImportNames = mutableSetOf<String>()
    private var trailingNewline = false

    val imports = imports.also {
        for ((memberName, _) in imports) {
            val lastDotIndex = memberName.lastIndexOf('.')
            if (lastDotIndex >= 0) {
                memberImportNames.add(memberName.substring(0, lastDotIndex))
            }
        }
    }

    /**
     * When emitting a statement, this is the line of the statement currently being written. The first
     * line of a statement is indented normally and subsequent wrapped lines are double-indented. This
     * is -1 when the currently-written line isn't part of a statement.
     */
    var statementLine = -1

    fun indent(levels: Int = 1) = apply {
        indentLevel += levels
    }

    fun unindent(levels: Int = 1) = apply {
        require(indentLevel - levels >= 0) { "cannot unindent $levels from $indentLevel" }
        indentLevel -= levels
    }

    fun pushPackage(packageName: String) = apply {
        check(this.packageName === NO_PACKAGE) { "package already set: ${this.packageName}" }
        this.packageName = packageName
    }

    fun popPackage() = apply {
        check(packageName !== NO_PACKAGE) { "package already set: $packageName" }
        packageName = NO_PACKAGE
    }

    fun pushType(type: DartClassSpec) = apply {
        this.typeSpecStack.add(type)
    }

    fun popType() = apply {
        this.typeSpecStack.removeAt(typeSpecStack.size - 1)
    }

    fun emitComment(codeBlock: CodeBlock) {
        trailingNewline = true // Force the '//' prefix for the comment.
        comment = true
        try {
            emitCode(codeBlock)
            emit("\n")
        } finally {
            comment = false
        }
    }

    fun emitKdoc(kdocCodeBlock: CodeBlock) {
        if (kdocCodeBlock.isEmpty()) return

        emit("/**\n")
        kdoc = true
        try {
            emitCode(kdocCodeBlock, ensureTrailingNewline = true)
        } finally {
            kdoc = false
        }
        emit(" */\n")
    }

    fun emitAnnotations(annotations: List<AnnotationSpec>, inline: Boolean) {
        for (annotationSpec in annotations) {
           // annotationSpec.emit(this, inline)
            emit(if (inline) " " else "\n")
        }
    }

    /**
     * Emits `modifiers` in the standard order. Modifiers in `implicitModifiers` will not
     * be emitted except for [KModifier.PUBLIC]
     */
    fun emitModifiers(
        modifiers: Set<DartModifier>,
        implicitModifiers: Set<DartModifier> = emptySet(),
    ) {
        if (shouldEmitPublicModifier(modifiers, implicitModifiers)) {
            emit(DartModifier.PUBLIC.identifier)
            emit(" ")
        }
        val uniqueNonPublicExplicitOnlyModifiers =
            modifiers
                .filterNot { it == DartModifier.PUBLIC }
                .filterNot { implicitModifiers.contains(it) }
                .toEnumSet()
        for (modifier in uniqueNonPublicExplicitOnlyModifiers) {
            emit(modifier.identifier)
            emit(" ")
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
            val part = partIterator.next()
            println("part is $part")
            when (part) {
                "%L" -> emitLiteral(codeBlock.args[a++], isConstantContext)

                "%N" -> emit(codeBlock.args[a++] as String)

                "%S" -> {
                    val string = codeBlock.args[a++] as String?
                    // Emit null as a literal null: no quotes.
                    val literal = if (string != null) {
                        stringLiteralWithQuotes(
                            string,
                            isInsideRawString = false,
                            isConstantContext = isConstantContext,
                        )
                    } else {
                        "null"
                    }
                    emit(literal, nonWrapping = true)
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
                        "null"
                    }
                    emit(literal, nonWrapping = true)
                }

             /*   "%T" -> {
                    var typeName = codeBlock.args[a++] as TypeName
                    if (typeName.isAnnotated) {
                        typeName.emitAnnotations(this)
                        typeName = typeName.copy(annotations = emptyList())
                    }
                    // defer "typeName.emit(this)" if next format part will be handled by the default case
                    var defer = false
                    if (typeName is ClassName && partIterator.hasNext()) {
                        if (!codeBlock.formatParts[partIterator.nextIndex()].startsWith("%")) {
                            val candidate = typeName
                            if (candidate.canonicalName in memberImportNames) {
                                check(deferredTypeName == null) { "pending type for static import?!" }
                                deferredTypeName = candidate
                                defer = true
                            }
                        }
                    }
                    if (!defer) typeName.emit(this)
                    typeName.emitNullable(this)
                }*/

                "%M" -> {
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
                    // Handle deferred type.
                    var doBreak = false
                    if (!doBreak) {
                        emit(part)
                    }
                }
            }
        }
        if (ensureTrailingNewline && out.hasPendingSegments) {
            emit("\n")
        }
    }

    private fun emitLiteral(o: Any?, isConstantContext: Boolean) {
        when (o) {
          /*  is TypeSpec -> o.emit(this, null)
            is AnnotationSpec -> o.emit(this, inline = true, asParameter = isConstantContext)
            is PropertySpec -> o.emit(this, emptySet())
            is FunSpec -> o.emit(
                codeWriter = this,
                enclosingName = null,
                implicitModifiers = setOf(KModifier.PUBLIC),
                includeKdocTags = true,
            )
            is TypeAliasSpec -> o.emit(this)
            is CodeBlock -> emitCode(o, isConstantContext = isConstantContext)*/
            else -> emit(o.toString())
        }
    }

    // TODO(luqasn): also honor superclass members when resolving names.

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
                if ((kdoc || comment) && trailingNewline) {
                    emitIndentation()
                    out.appendNonWrapping(if (kdoc) " *" else "//")
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
                if (kdoc) {
                    out.appendNonWrapping(" * ")
                } else if (comment) {
                    out.appendNonWrapping("// ")
                }
            }

            if (nonWrapping) {
                out.appendNonWrapping(line)
            } else {
                out.append(
                    line,
                    indentLevel = if (kdoc) indentLevel else indentLevel + 2,
                    linePrefix = if (kdoc) " * " else "",
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
     * Returns whether a [KModifier.PUBLIC] should be emitted.
     *
     * If [modifiers] contains [KModifier.PUBLIC], this method always returns `true`.
     *
     * Otherwise, this will return `true` when [KModifier.PUBLIC] is one of the [implicitModifiers]
     * and there are no other opposing modifiers (like [KModifier.PROTECTED] etc.) supplied by the
     * consumer in [modifiers].
     */
    private fun shouldEmitPublicModifier(
        modifiers: Set<DartModifier>,
        implicitModifiers: Set<DartModifier>,
    ): Boolean {
        if (modifiers.contains(DartModifier.PUBLIC)) {
            return true
        }

        if (!implicitModifiers.contains(DartModifier.PUBLIC)) {
            return false
        }

        val hasOtherConsumerSpecifiedVisibility = true

        return !hasOtherConsumerSpecifiedVisibility
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

    override fun close() {
        out.close()
    }
}
