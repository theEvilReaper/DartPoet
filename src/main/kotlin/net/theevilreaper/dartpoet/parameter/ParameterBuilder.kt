package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
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
    val type: ParameterType = ParameterType.POSITIONAL,
    val typeName: TypeName?,
) {
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal var named: Boolean = false
    internal var nullable: Boolean = false
    internal var initializer: CodeBlock? = null

    /**
     * Add some manual code parts to the initializer block.
     * @param format the format string
     * @param args the arguments to replace in the format string
     * @return the current [ParameterBuilder] instance
     */
    fun initializer(format: String, vararg args: Any) = apply {
        initializer(CodeBlock.of(format, *args))
    }

    /**
     * Add a given [CodeBlock] to the initializer block.
     * @param block the [CodeBlock] to add
     * @return the current [ParameterBuilder] instance
     */
    fun initializer(block: CodeBlock) = apply {
        this.initializer = block
    }

    /**
     * Indicates whether the parameter is nullable or not.
     * @param nullable true if the parameter is nullable, false otherwise
     * @return the current [ParameterBuilder] instance
     */
    fun nullable(nullable: Boolean) = apply {
        this.nullable = nullable
    }

    /**
     * Add a given annotation via lambda reference to the parameter.
     * @param annotation the lambda reference to the annotation
     * @return the current [ParameterBuilder] instance
     */
    fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.annotations += annotation()
    }

    /**
     * Adds the given annotation to the parameter.
     * @param annotation the annotation to add
     * @return the current [ParameterBuilder] instance
     */
    fun annotation(annotation: AnnotationSpec) = apply {
        this.annotations += annotation
    }

    /**
     * Adds the given annotations to the parameter.
     * @param annotations the annotations to add
     * @return the current [ParameterBuilder] instance
     */
    fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotations.addAll(annotations)
    }

    /**
     * This method constructs a [ParameterSpec] object using the settings and data defined in the associated
     * [ParameterBuilder]. It is typically used to create a parameter specification after configuring it
     * with the desired parameter details.
     *
     * @return a [ParameterSpec] instance representing the parameter specification
     */
    fun build(): ParameterSpec = ParameterSpec(this)
}