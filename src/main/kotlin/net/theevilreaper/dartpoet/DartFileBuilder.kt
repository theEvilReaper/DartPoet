package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeFragment
import net.theevilreaper.dartpoet.code.CodeFragmentBuilder
import net.theevilreaper.dartpoet.import.Import
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import java.lang.IllegalArgumentException

class DartFileBuilder(
    val name: String
) {
    internal val comment: CodeFragmentBuilder = CodeFragment.builder()
    internal val specTypes: MutableList<Any> = mutableListOf()
    internal val imports: MutableList<Import> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
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

    fun addType(dartFileSpec: DartFile) = apply {
        this.specTypes += dartFileSpec
    }

    fun addType(dartFileSpec: () -> DartFile) = apply {
        this.specTypes += dartFileSpec()
    }

    fun addType(dartFileSpec: () -> DartFileBuilder) = apply {
        this.specTypes += dartFileSpec()
    }

    fun addType(dartFileSpec: DartFileBuilder) = apply {
        this.specTypes += dartFileSpec.build()
    }

    fun annotations(annotations: Iterable<AnnotationSpec>) = apply {
        this.annotations += annotations
    }

    fun annotations(annotations: () -> Iterable<AnnotationSpec>) = apply {
        this.annotations += annotations()
    }

    fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.annotations += annotation()
    }

    fun annotation(annotation: AnnotationSpec) = apply {
        this.annotations += annotation
    }

    fun build(): DartFile {
        return DartFile(this)
    }
}