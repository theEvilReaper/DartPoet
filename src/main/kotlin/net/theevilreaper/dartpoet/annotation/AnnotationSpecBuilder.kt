package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.CodeBlock


/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

class AnnotationSpecBuilder(
    val name: String
) {

    internal val content: MutableList<CodeBlock> = mutableListOf()

    fun content(format: String, vararg args: Any) = apply {
        content(CodeBlock.of(format, args))
    }

    fun content(codeFragment: CodeBlock) = apply {
        this.content += codeFragment
    }

    fun content(codeFragment: () -> CodeBlock) = apply {
        this.content += codeFragment()
    }

    fun build(): AnnotationSpec {
        return AnnotationSpec(this)
    }
}