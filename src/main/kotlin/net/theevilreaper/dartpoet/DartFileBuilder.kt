package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.directive.Directive
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.meta.AnnotationData
import net.theevilreaper.dartpoet.meta.AnnotationMethods
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import net.theevilreaper.dartpoet.util.isIndent

class DartFileBuilder(
    val name: String
) : AnnotationMethods<DartFileBuilder> {
    internal val docs: MutableList<CodeBlock> = mutableListOf()
    internal val specTypes: MutableList<TypeSpec> = mutableListOf()
    internal val directives: MutableList<Directive> = mutableListOf()
    internal val annotationData: AnnotationData = AnnotationData()
    internal val extensionStack: MutableList<ExtensionSpec> = mutableListOf()
    internal val constants: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal val typeDefs: MutableList<AbstractTypeDef<*>> = mutableListOf()
    internal val propertyStack: MutableList<PropertySpec> = mutableListOf()
    internal val functionStack: MutableList<FunctionSpec> = mutableListOf()
    internal var indent = DEFAULT_INDENT

    /**
     * Add a top level [ConstantPropertySpec] to the file.
     * @param constant the constant to add
     * @return the current instance of [DartFileBuilder]
     */
    fun constant(constant: ConstantPropertySpec) = apply {
        this.constants += constant
    }

    /**
     * Add an array of top level [ConstantPropertySpec]s to the file.
     * @param constants the constants to add
     * @return the current instance of [DartFileBuilder]
     */
    fun constants(vararg constants: ConstantPropertySpec) = apply {
        this.constants += constants
    }

    /**
     * Add a top level [PropertySpec] to the file.
     * @param property the property to add
     * @return the current instance of [DartFileBuilder]
     */
    fun property(property: PropertySpec) = apply {
        this.propertyStack += property
    }

    /**
     * Add a top level [PropertySpec] to the file over a lambda reference.
     * @param property lambda returning the property to add
     * @return the current instance of [DartFileBuilder]
     */
    fun property(property: () -> PropertySpec) = this.property(property())

    /**
     * Add an array of top level [PropertySpec] to the file.
     * @param properties the properties to add
     * @return the current instance of [DartFileBuilder]
     */
    fun properties(vararg properties: PropertySpec) = apply {
        this.propertyStack += properties
    }

    /**
     * Add a top level [FunctionSpec] to the file.
     * @param function the function to add
     * @return the current instance of [DartFileBuilder]
     */
    fun function(function: FunctionSpec) = apply {
        this.functionStack += function
    }

    /**
     * Add a top level [FunctionSpec] to the file over a lambda reference.
     * @param function lambda returning the function to add
     * @return the current instance of [DartFileBuilder]
     */
    fun function(function: () -> FunctionSpec) = this.function(function())

    /**
     * Add an array of top level [FunctionSpec] to the file.
     * @param functions the functions to add
     * @return the current instance of [DartFileBuilder]
     */
    fun functions(vararg functions: FunctionSpec) = apply {
        this.functionStack += functions
    }

    /**
     * Add a [Directive] (e.g. an import, export, part or library directive) to the file.
     * The directives are sorted by their type when the file is written.
     * @param directive the directive to add
     * @return the current instance of [DartFileBuilder]
     */
    fun directive(directive: Directive) = apply {
        this.directives += directive
    }

    /**
     * Add a [Directive] to the file over a lambda reference.
     * @param directive lambda returning the directive to add
     * @return the current instance of [DartFileBuilder]
     */
    fun directive(directive: () -> Directive) = apply {
        this.directives += directive()
    }

    /**
     * Add an array of [Directive]s to the file.
     * @param directive the directives to add
     * @return the current instance of [DartFileBuilder]
     */
    fun directives(vararg directive: Directive) = apply {
        this.directives += directive
    }

    /**
     * Add a documentation comment at the top of the file.
     * Spaces are replaced with non-breaking spaces (`·`) so that the comment line is not wrapped.
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     * @return the current instance of [DartFileBuilder]
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Set the indent which is used to write the file.
     * @param indent the indent to use, which may only contain spaces
     * @return the current instance of [DartFileBuilder]
     * @throws IllegalStateException if the indent contains other characters than spaces
     */
    fun indent(indent: String) = apply {
        check(isIndent(indent)) { "An indent can only contains only spaces" }
        this.indent = indent
    }

    /**
     * Set the indent which is used to write the file over a lambda reference.
     * @param indent lambda returning the indent to use, which may only contain spaces
     * @return the current instance of [DartFileBuilder]
     * @throws IllegalStateException if the indent contains other characters than spaces
     */
    fun indent(indent: () -> String) = apply {
        this.indent(indent())
    }

    /**
     * Add a top level [ExtensionSpec] to the file.
     * @param extension the extension to add
     * @return the current instance of [DartFileBuilder]
     */
    fun extension(extension: ExtensionSpec) = apply {
        this.extensionStack += extension
    }

    /**
     * Add a top level [ExtensionSpec] to the file over a lambda reference.
     * @param extension lambda returning the extension to add
     * @return the current instance of [DartFileBuilder]
     */
    fun extension(extension: () -> ExtensionSpec) = apply {
        this.extensionStack += extension()
    }

    /**
     * Add an array of top level [ExtensionSpec]s to the file.
     * @param extensions the extensions to add
     * @return the current instance of [DartFileBuilder]
     */
    fun extensions(vararg extensions: ExtensionSpec) = apply {
        this.extensionStack += extensions
    }

    /**
     * Add a type definition to the file builder.
     * @param typeDef the type definition to add
     * @return the current instance of [DartFileBuilder]
     */
    fun typeDef(typeDef: AbstractTypeDef<*>) = apply {
        this.typeDefs += typeDef
    }

    /**
     * Add an array of type definitions to the file builder.
     * @param typeDef the type definitions to add
     * @return the current instance of [DartFileBuilder]
     */
    fun typeDef(vararg typeDef: AbstractTypeDef<*>) = apply {
        this.typeDefs += typeDef
    }

    /**
     * Add a top level [TypeSpec] (e.g. [net.theevilreaper.dartpoet.clazz.ClassSpec], [net.theevilreaper.dartpoet.enum.EnumSpec], [net.theevilreaper.dartpoet.mixin.MixinSpec]) to the file.
     * @param typeSpec the type specification to add
     * @return the current instance of [DartFileBuilder]
     */
    fun type(typeSpec: TypeSpec) = apply {
        this.specTypes += typeSpec
    }

    /**
     * Add a top level [TypeSpec] to the file over a lambda reference.
     * @param typeSpec lambda returning the type specification to add
     * @return the current instance of [DartFileBuilder]
     */
    fun type(typeSpec: () -> TypeSpec) = this.type(typeSpec())

    /**
     * Add an array of top level [TypeSpec]s to the file.
     * @param typeSpecs the type specifications to add
     * @return the current instance of [DartFileBuilder]
     */
    fun type(vararg typeSpecs: TypeSpec) = apply {
        this.specTypes += typeSpecs
    }

    /**
     * Add an array of top level [TypeSpec]s to the file.
     * @param typeSpecs the type specifications to add
     * @return the current instance of [DartFileBuilder]
     */
    fun types(vararg typeSpecs: TypeSpec) = apply {
        this.specTypes += typeSpecs
    }

    /**
     * Add a new [AnnotationSpec] to the file via lambda reference.
     * @param annotation the lambda reference to the annotation
     * @return the current [DartFileBuilder] instance
     */
    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Add a new [AnnotationSpec] to the file.
     * @param annotation the annotation to add
     * @return the current [DartFileBuilder] instance
     */
    override fun annotation(annotation: AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Add a given array of [AnnotationSpec] to the file.
     * @param annotations the annotations to add
     * @return the current [DartFileBuilder] instance
     */
    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotationData.annotations(*annotations)
    }

    /**
     * Creates a new reference from the [DartFile] class.
     * @return the created instance
     */
    fun build(): DartFile {
        return DartFile(this)
    }
}
