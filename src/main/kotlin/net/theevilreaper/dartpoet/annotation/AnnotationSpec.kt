package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.AnnotationWriter
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
class AnnotationSpec(
    builder: AnnotationSpecBuilder
) {

    internal val name: String = builder.name
    internal val content: Set<CodeBlock> = builder.content.toImmutableSet()
    internal val hasMultipleContentParts = content.size > 1

    init {
        check(name.trim().isNotEmpty()) { "The name can't be empty" }
    }

    internal fun write(
        codeWriter: CodeWriter,
        inline: Boolean = true
    ) {
        AnnotationWriter().emit(this, codeWriter, inline = inline)
    }

    override fun toString() = buildCodeString {
        write(
            this
        )
    }

    companion object {

        @JvmStatic
        fun builder(name: String) = AnnotationSpecBuilder(name)
    }
}