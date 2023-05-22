package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.DartClassBuilder
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.import.Import
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import java.lang.IllegalArgumentException

class DartFileBuilder(
    val name: String
) {
    internal val comment: MutableList<CodeBlock> = mutableListOf()
    internal val specTypes: MutableList<DartClassSpec> = mutableListOf()
    internal val imports: MutableList<Import> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal val extensionStack: MutableList<ExtensionSpec> = mutableListOf()
    internal var indent = DEFAULT_INDENT

    fun fileComment(format: String, vararg args: Any) = apply {
        this.comment.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    fun import(import: Import) = apply {
        this.imports += import
    }

    fun import(import: () -> Import) = apply {
        this.imports += import()
    }

    fun imports(import: Iterable<Import>) = apply {
        this.imports += import
    }

    fun imports(import: () -> Iterable<Import>) = apply {
        this.imports += import()
    }

    fun indent(indent: String) = apply {
        if (indent.trim().isEmpty()) {
            throw IllegalArgumentException("The indent can't be empty")
        }
        this.indent = indent
    }

    fun indent(indent: () -> String) = apply {
        this.indent(indent())
    }

    fun extension(extension: ExtensionSpec) = apply {
        this.extensionStack += extension
    }

    fun extension(extension: () -> ExtensionSpec) = apply {
        this.extensionStack += extension()
    }

    fun extensions(vararg extensions: ExtensionSpec) = apply {
        this.extensionStack += extensions
    }

    fun type(dartFileSpec: DartClassSpec) = apply {
        this.specTypes += dartFileSpec
    }

    fun type(dartFileSpec: () -> DartClassSpec) = apply {
        this.specTypes += dartFileSpec()
    }

    fun type(dartFileSpec: DartClassBuilder) = apply {
        this.specTypes += dartFileSpec.build()
    }

    fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotations += annotations
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
