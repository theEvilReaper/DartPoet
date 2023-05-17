package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.DartClassBuilder
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.import.Import
import net.theevilreaper.dartpoet.property.DartPropertySpec
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import java.lang.IllegalArgumentException

class DartFileBuilder(
    val name: String
) {
    internal val comment: CodeBlock.Builder = CodeBlock.builder()
    internal val specTypes: MutableList<DartClassSpec> = mutableListOf()
    internal val imports: MutableList<Import> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal val extensionStack: MutableList<ExtensionSpec> = mutableListOf()
    internal var indent = DEFAULT_INDENT
    internal val constants: MutableSet<DartPropertySpec> = mutableSetOf()

    /**
     * Add a constant [DartPropertySpec] to the file.
     * @param constant the property to add
     */
    fun constant(constant: DartPropertySpec) = apply {
        this.constants += constant
    }

    /**
     * Add an array of constant [DartPropertySpec] to the file.
     * @param constants the array to add
     */
    fun constants(vararg constants: DartPropertySpec) = apply {
        this.constants += constants
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

    fun type(vararg classSpecs: DartClassSpec) = apply {
        this.specTypes += classSpecs
    }

    fun type(dartFileSpec: DartClassBuilder) = apply {
        this.specTypes += dartFileSpec.build()
    }

    fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotations += annotations
    }

    fun annotation(vararg annotations: AnnotationSpec) = apply {
        this.annotations += annotations
    }

    fun annotation(annotation: AnnotationSpec) = apply {
        this.annotations += annotation
    }

    /**
     * Creates a new reference from the [DartFile] class.
     * @return the created instance
     */
    fun build(): DartFile {
        return DartFile(this)
    }
}
