package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.joinToCode
import net.theevilreaper.dartpoet.util.ANNOTATION_CHAR
import net.theevilreaper.dartpoet.util.toImmutableList

class AnnotationWriter {

    fun emit(annotationSpec: AnnotationSpec, writer: CodeWriter, inline: Boolean) {
        writer.emit("${ANNOTATION_CHAR}${annotationSpec.name}")

        if (annotationSpec.content.isEmpty()) return

        val whitespace = if (inline) "" else "\n"
        val memberSeparator = if (inline) ", " else ",\n"
        val memberSuffix = if (!inline && annotationSpec.content.size > 1) "," else ""

        writer.emit("(")
        if (annotationSpec.content.size > 1) writer.emit(whitespace).indent()
        writer.emitCode(
            codeBlock = annotationSpec.content.toImmutableList()
                .map { if (inline) it.replaceAll("[⇥|⇤]", "") else it }
                .joinToCode(separator = memberSeparator, suffix = memberSuffix),
            isConstantContext = true,
        )
        if (annotationSpec.content.size > 1) writer.unindent().emit(whitespace)
        writer.emit(")")
    }
}