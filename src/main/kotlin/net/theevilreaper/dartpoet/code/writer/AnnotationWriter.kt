package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.joinToCode
import net.theevilreaper.dartpoet.util.ANNOTATION_CHAR
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.ROUND_CLOSE
import net.theevilreaper.dartpoet.util.ROUND_OPEN
import net.theevilreaper.dartpoet.util.toImmutableList

/**
 * The [AnnotationWriter] is responsible for writing the data from an [AnnotationSpec] into valid code for dart code.
 * @since 1.0.0
 * @author theEvilReaper
 */
internal class AnnotationWriter {

    /**
     * Writes the data which are stored in a [AnnotationSpec] to a given instance from a [CodeWriter].
     * @param spec the spec to write
     * @param writer the writer to write the spec to
     * @param inline if the spec should be written inline
     */
    fun emit(spec: AnnotationSpec, writer: CodeWriter, inline: Boolean) {
        writer.emit(ANNOTATION_CHAR)
        writer.emitCode("%T", spec.typeName)

        if (!spec.hasContent) return

        val whitespace = if (inline) EMPTY_STRING else NEW_LINE
        val memberSeparator = if (inline) COMMA_SEPARATOR else ",\n"
        val memberSuffix = if (!inline && spec.content.size > 1) "," else EMPTY_STRING

        writer.emit(ROUND_OPEN)
        if (spec.hasMultipleContentParts) writer.emit(whitespace).indent()
        writer.emitCode(
            codeBlock = mapCodeBlocks(spec, inline, memberSeparator, memberSuffix),
            isConstantContext = true,
        )
        if (spec.hasMultipleContentParts) writer.unindent().emit(whitespace)
        writer.emit(ROUND_CLOSE)
    }

    /**
     * Maps the content from an [AnnotationSpec] into a [CodeBlock] reference.
     * @param spec the spec to map
     * @param inline if the spec should be written inline
     * @param memberSeparator the separator for the members
     * @param memberSuffix the suffix for the members
     * @return the created [CodeBlock] reference
     */
    private fun mapCodeBlocks(
        spec: AnnotationSpec,
        inline: Boolean,
        memberSeparator: String,
        memberSuffix: String,
    ): CodeBlock {
        return spec.content.toImmutableList()
            .map { if (inline) it.replaceAll("[⇥|⇤]", EMPTY_STRING) else it }
            .joinToCode(separator = memberSeparator, suffix = memberSuffix)
    }
}