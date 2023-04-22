package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.joinToCode
import net.theevilreaper.dartpoet.util.ANNOTATION_CHAR
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.toImmutableList

class AnnotationWriter {

    fun emit(spec: AnnotationSpec, writer: CodeWriter, inline: Boolean) {
        writer.emit("${ANNOTATION_CHAR}${spec.name}")

        if (spec.content.isEmpty()) return

        val whitespace = if (inline) EMPTY_STRING else NEW_LINE
        val memberSeparator = if (inline) ", " else ",\n"
        val memberSuffix = if (!inline && spec.content.size > 1) "," else EMPTY_STRING

        writer.emit("(")
        if (spec.hasMultipleContentParts) writer.emit(whitespace).indent()
        writer.emitCode(
            codeBlock = mapCodeBlocks(spec, inline, memberSeparator, memberSuffix),
            isConstantContext = true,
        )
        if (spec.hasMultipleContentParts) writer.unindent().emit(whitespace)
        writer.emit(")")
    }

    private fun mapCodeBlocks(spec: AnnotationSpec, inline: Boolean, memberSeparator: String, memberSuffix: String): CodeBlock {
        return spec.content.toImmutableList()
            .map { if (inline) it.replaceAll("[⇥|⇤]", EMPTY_STRING) else it }
            .joinToCode(separator = memberSeparator, suffix = memberSuffix)
    }
}