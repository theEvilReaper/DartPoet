package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.TypeName

/**
 * With the AnnotationBuilder data can be set to an annotation.
 * These are later read out during generation and converted into code accordingly.
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
class AnnotationSpecBuilder(
    internal val typeName: TypeName
) {

    internal val content: MutableList<CodeBlock> = mutableListOf()

    fun content(format: String, vararg args: Any) = apply {
        content(CodeBlock.of(format, *args))
    }

    fun content(codeFragment: CodeBlock) = apply {
        this.content += codeFragment
    }

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
