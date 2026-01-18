package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.directive.Directive
import net.theevilreaper.dartpoet.function.typedef.alias.TypeDefSpec
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import net.theevilreaper.dartpoet.util.isIndent

class DartFileBuilder(
    val name: String
) {
    internal val docs: MutableList<CodeBlock> = mutableListOf()
    internal val specTypes: MutableList<ClassSpec> = mutableListOf()
    internal val directives: MutableList<Directive> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal val extensionStack: MutableList<ExtensionSpec> = mutableListOf()
    internal val constants: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal val typeDefs: MutableList<TypeDefSpec> = mutableListOf()
    internal var indent = DEFAULT_INDENT

    /**
     * Add a constant [PropertySpec] to the file.
     * @param constant the property to add
     */
    fun constant(constant: ConstantPropertySpec) = apply {
        this.constants += constant
    }

    fun constants(vararg constants: ConstantPropertySpec) = apply {
        this.constants += constants
    }

    fun directive(directive: Directive) = apply {
        this.directives += directive
    }

    fun directive(directive: () -> Directive) = apply {
        this.directives += directive()
    }

    fun directives(vararg directive: Directive) = apply {
        this.directives += directive
    }

    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    fun indent(indent: String) = apply {
        check(isIndent(indent)) { "An indent can only contains only spaces" }
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

    /**
     * Add a type definition to the file builder.
     * @param typeDef the type definition to add
     * @return the current instance of [DartFileBuilder]
     */
    fun typeDef(typeDef: TypeDefSpec) = apply {
        this.typeDefs += typeDef
    }

    /**
     * Add an array of type definitions to the file builder.
     * @param typeDef the type definitions to add
     * @return the current instance of [DartFileBuilder]
     */
    fun typeDef(vararg typeDef: TypeDefSpec) = apply {
        this.typeDefs += typeDef
    }

    fun type(dartFileSpec: ClassSpec) = apply {
        this.specTypes += dartFileSpec
    }

    fun type(vararg classSpecs: ClassSpec) = apply {
        this.specTypes += classSpecs
    }

    fun type(dartFileSpec: ClassBuilder) = apply {
        this.specTypes += dartFileSpec.build()
    }

    fun annotations(vararg annotations: AnnotationSpec) = apply {
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
