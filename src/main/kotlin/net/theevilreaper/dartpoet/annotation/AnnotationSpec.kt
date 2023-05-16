package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.AnnotationWriter
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * The [AnnotationSpec] contain all relevant data about a annotation.
 *
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

    /**
     * Performs some check calls on some variables.
     */
    init {
        check(name.trim().isNotEmpty()) { "The name can't be empty" }
    }

    /**
     * Triggers an [AnnotationWriter] to write the spec object into code.
     */
    internal fun write(
        codeWriter: CodeWriter,
        inline: Boolean = true
    ) {
        AnnotationWriter().emit(this, codeWriter, inline = inline)
    }

    /**
     * Returns a string representation of the [AnnotationSpec].
     * The method triggers the [write] method to get the spec object as string
     * @return the created string representation
     */
    override fun toString() = buildCodeString { write(this) }

    companion object {

        /**
         * Creates a new instance from the [AnnotationSpecBuilder].
         * @return the created instance
         */
        @JvmStatic
        fun builder(name: String) = AnnotationSpecBuilder(name)
    }
}
