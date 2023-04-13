package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.AnnotationWriter
import net.theevilreaper.dartpoet.util.ANNOTATION_CHAR
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

val annotationWriter = AnnotationWriter()

class AnnotationSpec(
    builder: AnnotationSpecBuilder
) {

    internal val name: String = builder.name
    internal val content: Set<CodeBlock> = builder.content.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name can't be empty" }
    }

    fun emit(codeWriter: CodeWriter) {

    }

    //TODO: Update later to the class which writes the code. For now it is used to test the generation
    fun write(): String {
        val builder = StringBuilder()
        builder.append("$ANNOTATION_CHAR$name")

        if (content.isEmpty()) {
            return builder.toString()
        }

        builder.append("(")
        //TODO: Write content parts
        builder.append(")")

        return builder.toString()
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        AnnotationWriter().emit(this, codeWriter, inline = true)
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