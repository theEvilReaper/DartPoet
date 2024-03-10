package net.theevilreaper.dartpoet.property.consts

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.TypeName

/**
 * The [ConstantPropertyBuilder] can be used to construct new [ConstantPropertySpec] object reference which can be set
 * into a [ConstantPropertySpec].
 * @author theEvilReaper
 * @since 1.0.0
 */
class ConstantPropertyBuilder internal constructor(
    val name: String,
    val typeName: TypeName? = null,
    val modifiers: Set<DartModifier>
) {
    internal var initializer: CodeBlock.Builder = CodeBlock.Builder()
    internal var isPrivate: Boolean = false

    /**
     * Apply a given format which contains the parts for the init block of the [PropertySpec].
     * @param format the given format
     * @param args the arguments for the format
     */
    fun initWith(format: String, vararg args: Any?) = apply {
        this.initializer.add(format, *args)
    }

    /**
     * Set the initializer block directly as [CodeBlock.Builder] to the property.
     * @param codeFragment the [CodeBlock.Builder] to set
     */
    fun initWith(codeFragment: CodeBlock.Builder) = apply {
        this.initializer = codeFragment
    }

    /**
     * Indicates if the property should be private.
     * The option is only allowed when to property is no file level constant property.
     * @param boolean True for a private property otherwise false
     * @return the current [ConstantPropertyBuilder] instance
     */
    fun private(boolean: Boolean) = apply {
        this.isPrivate = boolean
    }

    /**
     * Constructs a [ConstantPropertySpec] reference from the current builder instance
     * @return the created [ConstantPropertySpec] instance
     */
    fun build(): ConstantPropertySpec {
        return ConstantPropertySpec(this)
    }
}
