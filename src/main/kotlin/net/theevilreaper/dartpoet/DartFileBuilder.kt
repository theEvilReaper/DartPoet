package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import java.lang.IllegalArgumentException

class DartFileBuilder(
    val name: String
) : SpecMethods<DartFileBuilder> {

    internal val specData = SpecData()
    internal val documentation: MutableList<String> = mutableListOf()
    internal var indent = DEFAULT_INDENT

    fun indent(indent: String) = apply {
        if (indent.trim().isEmpty()) {
            throw IllegalArgumentException("The indent can't be empty")
        }
        this.indent = indent
    }

    fun indent(indent: () -> String) = apply {
        this.indent(indent())
    }

    override fun annotations(annotations: Iterable<AnnotationSpec>) = apply {
        this.specData.annotations(annotations)
    }

    override fun annotations(annotations: () -> Iterable<AnnotationSpec>) = apply {
        this.specData.annotations(annotations)
    }

    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun annotation(annotation: AnnotationSpec) = apply {
        this.specData.annotation(annotation)
    }

    override fun modifier(modifier: DartModifier) = apply {
        this.specData.modifier(modifier)
    }

    override fun modifier(modifier: () -> DartModifier) = apply {
        this.specData.modifier(modifier)
    }

    override fun modifiers(vararg modifiers: DartModifier) = apply {
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun modifiers(modifiers: Iterable<DartModifier>) = apply {
        this.specData.modifiers(modifiers)
    }

    override fun modifiers(modifiers: () -> Iterable<DartModifier>) = apply {
        this.specData.modifiers(modifiers)
    }

    fun build(): DartFile {
        return DartFile(this)
    }
}
