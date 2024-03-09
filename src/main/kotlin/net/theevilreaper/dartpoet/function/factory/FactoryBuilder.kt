package net.theevilreaper.dartpoet.function.factory

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.ConstructorDelegation
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName

/**
 * The [FactoryBuilder] is responsible for configuring and assembling details of a factory constructor.
 * @param typeName the type name for the factory constructor
 * @since 1.0.0
 * @author theEvilReaper
 */
class FactoryBuilder(
    val typeName: TypeName,
    val const: Boolean = false,
) {
    internal val documentation: CodeBlock.Builder = CodeBlock.builder()
    internal val annotations: MutableSet<AnnotationSpec> = mutableSetOf()
    internal val parameters: MutableSet<ParameterSpec> = mutableSetOf()
    internal val initializerBlock: CodeBlock.Builder = CodeBlock.builder()
    internal var invokeType: ConstructorDelegation = ConstructorDelegation.NONE
    internal var namedString: String? = null

    /**
     * Add a new content for the documentation.
     * @param format the format string
     * @param args the arguments for the format string
     * @return the current [FactoryBuilder] instance
     */
    fun doc(format: String, vararg args: Any) = apply {
        documentation.add(format, *args)
    }

    /**
     * Add a new [AnnotationSpec] to the factory constructor.
     * @param annotation the annotation to add
     * @return the current [FactoryBuilder] instance
     */
    fun annotation(annotation: AnnotationSpec) = apply {
        annotations.add(annotation)
    }

    /**
     * Add a given array of [AnnotationSpec] to the factory constructor.
     * @param annotations the annotations to add
     * @return the current [FactoryBuilder] instance
     */
    fun annotation(vararg annotations: AnnotationSpec) = apply {
        this.annotations.addAll(annotations)
    }

    /**
     * Add a new [ParameterSpec] to the factory constructor.
     * @param parameter the parameter to add
     * @return the current [FactoryBuilder] instance
     */
    fun parameter(parameter: ParameterSpec) = apply {
        parameters.add(parameter)
    }

    /**
     * Add a given array of [ParameterSpec] to the factory constructor.
     * @param parameters the parameters to add
     * @return the current [FactoryBuilder] instance
     */
    fun parameter(vararg parameters: ParameterSpec) = apply {
        this.parameters.addAll(parameters)
    }

    /**
     * Set the delegation type for the factory constructor.
     * @param type the type of the delegation
     * @return the current [FactoryBuilder] instance
     */
    fun delegation(type: ConstructorDelegation) = apply {
        if (this.invokeType == type) return@apply
        this.invokeType = type
    }

    /**
     * Add a format string with arguments as initializer.
     * @param format the format for the block
     * @param args the arguments for the format
     */
    fun addCode(format: String, vararg args: Any?) = apply {
        initializerBlock.add(format, *args)
    }

    /**
     * Add a format string with arguments as initializer.
     * @param format the format for the block
     * @param args the arguments for the format
     */
    fun addNamedCode(format: String, args: Map<String, *>) = apply {
        initializerBlock.addNamed(format, args)
    }

    /**
     * Set's the named part of a factory constructor.
     * @param named the name for the factory constructor
     * @return the current [FactoryBuilder] instance
     */
    fun named(named: String) = apply {
        this.namedString = named
    }

    /**
     * Add a [CodeBlock] which contains the structure for the initializer for a constructor.
     * @param codeBlock the block to add
     */
    fun addCode(codeBlock: CodeBlock) = apply {
        initializerBlock.add(codeBlock)
    }

    /**
     * Constructs a new [FactorySpec] reference with the current data.
     * @return the created [FactorySpec] instance
     */
    fun build(): FactorySpec = FactorySpec(this)
}
