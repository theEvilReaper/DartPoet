package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassBuilder
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.directive.Directive
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.util.DEFAULT_INDENT
import net.theevilreaper.dartpoet.util.isIndent

class DartFileBuilder(
    val name: String
) {
    internal val docs: MutableList<CodeBlock> = mutableListOf()
    internal val specTypes: MutableList<TypeSpec> = mutableListOf()
    internal val directives: MutableList<Directive> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal val extensionStack: MutableList<ExtensionSpec> = mutableListOf()
    internal val constants: MutableSet<ConstantPropertySpec> = mutableSetOf()
    internal val typeDefs: MutableList<AbstractTypeDef<*>> = mutableListOf()
    internal val propertyStack: MutableList<PropertySpec> = mutableListOf()
    internal val functionStack: MutableList<FunctionSpec> = mutableListOf()
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

    /**
     * Add a top level [PropertySpec] to the file.
     * @param property the property to add
     */
    fun property(property: PropertySpec) = apply {
        this.propertyStack += property
    }

    /**
     * Add a top level [PropertySpec] to the file over a lambda reference.
     * @param property the property to add
     */
    fun property(property: () -> PropertySpec) = this.property(property())

    /**
     * Add an array of top level [PropertySpec] to the file.
     * @param properties the properties to add
     */
    fun properties(vararg properties: PropertySpec) = apply {
        this.propertyStack += properties
    }

    /**
     * Add a top level [FunctionSpec] to the file.
     * @param function the function to add
     */
    fun function(function: FunctionSpec) = apply {
        this.functionStack += function
    }

    /**
     * Add a top level [FunctionSpec] to the file over a lambda reference.
     * @param function the function to add
     */
    fun function(function: () -> FunctionSpec) = this.function(function())

    /**
     * Add an array of top level [FunctionSpec] to the file.
     * @param functions the functions to add
     */
    fun functions(vararg functions: FunctionSpec) = apply {
        this.functionStack += functions
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

    @Deprecated(
        message = "Pass the built ClassSpec instead",
        replaceWith = ReplaceWith("type(dartFileSpec.build())")
    )
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
