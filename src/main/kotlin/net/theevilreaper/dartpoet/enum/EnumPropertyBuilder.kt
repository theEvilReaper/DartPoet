package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.TypeName

/**
 * Builder for creating instances of [EnumPropertySpec].
 * Contains methods to add parameter objects and set a generic cast to the property.
 * @param name the name for the property
 * @param genericValueCast the generic cast for the property
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
class EnumPropertyBuilder internal constructor(
    val name: String,
    val genericValueCast: TypeName? = null,
) {
    internal val parameters: MutableList<CodeBlock> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()

    /**
     * Adds a new [AnnotationSpec] instance to the property.
     * @param annotation the annotation to add
     * @return the builder instance
     */
    fun annotation(annotation: AnnotationSpec) = apply {
        this.annotations += annotation
    }

    /**
     * Adds multiple [AnnotationSpec] instances to the property.
     * @param annotations the annotations to add
     * @return the builder instance
     */
    fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotations += annotations
    }

    /**
     * Adds a new parameter to the enum property.
     * @param format The format for the parameter
     * @param args The arguments for the format
     * @return the builder instance
     */
    fun parameter(format: String, vararg args: Any) = apply {
        parameter(CodeBlock.of(format, *args))
    }

    /**
     * Add a new parameter as [CodeBlock] to the property.
     * @param block the [CodeBlock] to add
     * @return the builder instance
     */
    fun parameter(block: CodeBlock) = apply {
        this.parameters += block
    }

    /**
     * Creates a new instance from the [EnumPropertySpec] with the builder instance as parameter.
     * @return a created instance from an [EnumPropertySpec]
     */
    fun build(): EnumPropertySpec {
        return EnumPropertySpec(this)
    }
}
