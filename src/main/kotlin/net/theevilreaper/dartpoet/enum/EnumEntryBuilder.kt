package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.meta.AnnotationData
import net.theevilreaper.dartpoet.meta.AnnotationMethods
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import kotlin.reflect.KClass

/**
 * A builder class for constructing instances of [EnumEntrySpec].
 * This builder provides methods to add annotations, parameters, and set a generic cast type for the enum entry.
 *
 * The class supports the addition of multiple annotations and parameters, as well as setting a generic type using
 * either a [TypeName], [ClassName], or a [KClass]. Once all properties are set, it constructs an instance of [EnumEntrySpec].
 *
 * @param name The name of the enum entry being built.
 *
 * @version 1.0.0
 * @since 1.0.0
 *
 * @author theEvilReaper
 */
class EnumEntryBuilder(
    val name: String
) : AnnotationMethods<EnumEntryBuilder> {
    internal var genericValueCast: TypeName? = null
    internal val annotationData: AnnotationData = AnnotationData()
    internal val parameters: MutableList<EnumParameterSpec> = mutableListOf()

    /**
     * Adds a new [AnnotationSpec] instance to the property via lambda reference.
     * @param annotation the lambda reference to the annotation
     * @return the builder instance
     */
    override fun annotation(annotation: () -> AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Adds a new [AnnotationSpec] instance to the property.
     * @param annotation the annotation to add
     * @return the builder instance
     */
    override fun annotation(annotation: AnnotationSpec) = apply {
        this.annotationData.annotation(annotation)
    }

    /**
     * Adds multiple [AnnotationSpec] instances to the property.
     * @param annotations the annotations to add
     * @return the builder instance
     */
    override fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotationData.annotations(*annotations)
    }

    /**
     * Adds a new parameter to the enum property.
     * @param parameter the parameter to add
     * @return the builder instance
     */
    fun parameter(parameter: () -> EnumParameterSpec) = apply {
        this.parameters += parameter()
    }

    /**
     * Add a new parameter as [CodeBlock] to the property.
     * @param enumParameterSpec the parameter to add
     * @return the builder instance
     */
    fun parameter(enumParameterSpec: EnumParameterSpec) = apply {
        this.parameters += enumParameterSpec
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
     * Creates a new instance from the [EnumEntrySpec] with the builder instance as parameter.
     * @return a created instance from an [EnumEntrySpec]
     */
    fun build(): EnumEntrySpec = EnumEntrySpec(this)
}
