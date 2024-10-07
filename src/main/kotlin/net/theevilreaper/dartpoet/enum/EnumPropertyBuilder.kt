package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import kotlin.reflect.KClass

/**
 * Builder for creating instances of [EnumPropertySpec].
 * Contains methods to add parameter objects and set a generic cast to the property.
 * @param name the name for the property
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
class EnumPropertyBuilder(
    val name: String
) {
    internal var genericValueCast: TypeName? = null
    internal val parameters: MutableList<CodeBlock> = mutableListOf()
    internal val annotations: MutableList<AnnotationSpec> = mutableListOf()
    internal var useVariableName: Boolean = false

    fun useVariableName() = apply {
        useVariableName = !useVariableName
    }

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
     * Sets the generic cast to the enum property.
     * @param value The value to set as [TypeName]
     * @return the builder instance
     */
    fun generic(value: TypeName) = apply { this.genericValueCast = value }

    /**
     * Set the cast value for a property.
     * @param value the value to set as [ClassName]
     * @return the builder instance
     */
    fun generic(value: ClassName) = apply { this.genericValueCast = value }

    /**
     * Set the cast value for a property.
     * @param value the value to set as [TypeName]
     * @return the builder instance
     */
    fun generic(value: KClass<*>) = apply { this.genericValueCast = value.asClassName() }

    /**
     * Creates a new instance from the [EnumPropertySpec] with the builder instance as parameter.
     * @return a created instance from an [EnumPropertySpec]
     */
    fun build(): EnumPropertySpec {
        return EnumPropertySpec(this)
    }
}
