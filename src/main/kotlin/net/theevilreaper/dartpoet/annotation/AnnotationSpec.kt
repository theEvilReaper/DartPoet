package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.CodeFragment
import net.theevilreaper.dartpoet.util.ANNOTATION_CHAR
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

class AnnotationSpec(
    builder: AnnotationSpecBuilder
) {

    private val name: String = builder.name
    private val content: Set<CodeFragment> = builder.content.toImmutableSet()

    init {
        check(name.trim().isNotEmpty()) { "The name can't be empty" }
    }

    //TODO: Update later to the class which writes the code. For now it is used to test the generation
    fun write(): String {
        val builder = StringBuilder()
        builder.append("$ANNOTATION_CHAR $name")

        if (content.isEmpty()) {
            return builder.toString()
        }

        builder.append("(")
        //TODO: Write content parts
        builder.append(")")

        return builder.toString()
    }

    companion object {

        @JvmStatic
        fun builder(name: String) = AnnotationSpecBuilder(name)
    }
}