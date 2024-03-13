package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.EnumPropertyWriter
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
class EnumPropertySpec internal constructor(
    val builder: EnumPropertyBuilder
) {
    internal val name = builder.name
    internal val generic = builder.genericValueCast
    internal val hasGeneric = builder.genericValueCast != null
    internal val parameters = builder.parameters.toImmutableList()
    internal val hasParameter = builder.parameters.isNotEmpty()
    internal val annotations = builder.annotations.toImmutableSet()

    /**
     * Contains some checks for the variable.
     * When a variable doesn't pass the check an exception will be thrown.
     */
    init {
        check(name.trim().isNotEmpty()) { "The name of a EnumProperty can't be empty" }
    }


    internal fun write(codeWriter: CodeWriter) {
        EnumPropertyWriter().write(this, codeWriter)
    }

    /**
     * Creates a string representation from the spec object.
     * @return the created string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Converts a [EnumPropertySpec] to a [EnumPropertyWriter] instance.
     * @return the created instance
     */
    fun toBuilder(): EnumPropertyBuilder {
        val builder = EnumPropertyBuilder(this.name)
        builder.annotations.addAll(this.annotations)
        builder.genericValueCast = this.generic
        builder.parameters.addAll(this.parameters)
        return builder
    }

    /**
     * The companion object contains some helper methods to create a new instance of a [EnumPropertyBuilder].
     */
    companion object {

        /**
         * Creates a new instance from the [EnumPropertyBuilder] to construct a new property.
         * @param name the name for the property
         * @return the created instance from the [EnumPropertyBuilder]
         */
        @JvmStatic
        fun builder(name: String) = EnumPropertyBuilder(name)
    }
}
