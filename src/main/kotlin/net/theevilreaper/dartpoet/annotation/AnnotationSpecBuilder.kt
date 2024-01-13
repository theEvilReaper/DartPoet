package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.TypeName

/**
 * With the AnnotationBuilder data can be set to an annotation.
 * These are later read out during generation and converted into code accordingly.
 * @author theEvilReaper
 * @param typeName the name of the annotation
 * @version 1.0.0
 * @since
 **/
class AnnotationSpecBuilder(
    internal val typeName: TypeName
) {
    /**
     * Stores the content parts from the annotation.
     */
    internal val content: MutableList<CodeBlock> = mutableListOf()

    /**
     * Add a content part to the annotation.
     * @param format the format string
     * @param args the arguments for the format string
     * @return the given instance of an [AnnotationSpecBuilder]
     */
    fun content(format: String, vararg args: Any) = apply {
        content(CodeBlock.of(format, *args))
    }

    /**
     * Add a content part to the annotation.
     * @param codeFragment the code fragment to add provided as [CodeBlock]
     * @return the given instance of an [AnnotationSpecBuilder]
     */
    fun content(codeFragment: CodeBlock) = apply {
        this.content += codeFragment
    }

    /**
     * Add a content part to the annotation.
     * @param codeFragment the code fragment to add provided as lambda block
     * @return the given instance of an [AnnotationSpecBuilder]
     */
    fun content(codeFragment: () -> CodeBlock) = apply {
        this.content += codeFragment()
    }

    /**
     * Creates a new instance from the [AnnotationSpec].
     * @return the created instance
     */
    fun build(): AnnotationSpec {
        return AnnotationSpec(this)
    }
}
