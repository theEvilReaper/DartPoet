package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import kotlin.reflect.KClass

/**
 * The builder allows the creation of a [EnumPropertySpec].
 * It contains methods to add parameter objects and set a generic cast to the property.
 *
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

    fun annotation(annotation: AnnotationSpec) = apply {
        this.annotations += annotation
    }

    fun annotations(vararg annotations: AnnotationSpec) = apply {
        this.annotations += annotations
    }

    /**
     * Add a new parameter to the property.
     * @param format the given format
     * @param args the arguments for the format
     */
    fun parameter(format: String, vararg args: Any) = apply {
        parameter(CodeBlock.of(format, *args))
    }

    /**
     * Add a new parameter as [CodeBlock] to the property.
     * @param block the [CodeBlock] to add
     */
    fun parameter(block: CodeBlock) = apply {
        this.parameters += block
    }

    /**
     * Set the generic cast to the property.
     * The value can't be empty (spaces are not allowed)
     * @param value the value to set
     */
    fun generic(value: TypeName) = apply { this.genericValueCast = value }

    /**
     * Set the cast value for a property.
     * @param value the value to set as [ClassName]
     */
    fun generic(value: ClassName) = apply { this.genericValueCast = value }

    /**
     * Set the cast value for a property.
     * @param value the value to set as [TypeName]
     */
    fun generic(value: KClass<*>) = apply { this.genericValueCast = value.asClassName() }

    /**
     * Creates a new instance from the [EnumPropertySpec] with the builder instance as parameter.
     * @return the created object instance
     */
    fun build(): EnumPropertySpec {
        return EnumPropertySpec(this)
    }
}
