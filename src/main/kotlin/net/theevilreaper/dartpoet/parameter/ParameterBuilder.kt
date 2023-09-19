package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.meta.SpecData
import net.theevilreaper.dartpoet.meta.SpecMethods
import net.theevilreaper.dartpoet.type.TypeName

/**
 * [ParameterBuilder] is responsible for configuring and assembling details of a parameter, such as its name,
 * type, named status, required status, nullability, and initializer. It implements the [SpecMethods]
 * interface to provide methods for customizing parameter specifications.
 *
 * This class is typically used in code generation tasks to construct and customize parameter specifications
 * before creating instances of [ParameterSpec].
 *
 * @param name The name of the parameter
 * @param typeName The type of the parameter, represented as a [TypeName]
 * @author theEvilReaper
 * @since 1.0.0
 */
class ParameterBuilder internal constructor(
    val name: String,
    val typeName: TypeName?,
) : SpecMethods<ParameterBuilder> {
    internal val specData: SpecData = SpecData()
    internal var named: Boolean = false
    internal var required: Boolean = false
    internal var nullable: Boolean = false
    internal var initializer: CodeBlock? = null

    fun initializer(format: String, vararg args: Any) = apply {
        initializer(CodeBlock.of(format, *args))
    }

    fun initializer(block: CodeBlock) = apply {
        this.initializer = block
    }

    fun named(named: Boolean) = apply {
        this.named = named
    }

    fun nullable(nullable: Boolean) = apply {
        this.nullable = nullable
    }

    fun required(required: Boolean) = apply {
        this.required = required
    }

    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.specData.annotations += annotation()
    }

    override fun annotation(annotation: AnnotationSpec) = apply {
        this.specData.annotations += annotation
    }

    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.specData.annotations(*annotations)
    }

    override fun modifier(modifier: DartModifier) = apply {
        this.specData.modifiers += modifier
    }

    override fun modifier(modifier: () -> DartModifier) = apply {
        this.specData.modifiers += modifier()
    }

    override fun modifiers(vararg modifiers: DartModifier) = apply {
        TODO("Not yet implemented")
    }

    /**
     * This method constructs a [ParameterSpec] object using the settings and data defined in the associated
     * [ParameterBuilder]. It is typically used to create a parameter specification after configuring it
     * with the desired parameter details.
     *
     * @return a [ParameterSpec] instance representing the parameter specification
     */
    fun build(): ParameterSpec {
        return ParameterSpec(this)
    }
}