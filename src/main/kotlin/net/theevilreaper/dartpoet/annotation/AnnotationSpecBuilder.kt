package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.code.CodeFragment
import net.theevilreaper.dartpoet.code.CodeFragmentBuilder


/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

class AnnotationSpecBuilder(
    val name: String
) {

    internal val content: MutableList<CodeFragment> = mutableListOf()

    fun content(format: String, vararg args: Any) = apply {
        this.content += CodeFragmentBuilder.of(format, args)
    }

    fun content(codeFragment: CodeFragment) = apply {
        this.content += codeFragment
    }

    fun content(codeFragment: () -> CodeFragment) = apply {
        this.content += codeFragment()
    }

    fun build(): AnnotationSpec {
        return AnnotationSpec(this)
    }
}