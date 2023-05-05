package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.EnumPropertyWriter
import net.theevilreaper.dartpoet.util.toImmutableList

/**
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
class EnumPropertySpec(
    val builder: EnumPropertyBuilder
) {

    internal val name = builder.name
    internal val generic = builder.genericValueCast
    internal val hasGeneric = builder.genericValueCast.orEmpty().isNotEmpty()
    internal val parameters = builder.parameters.toImmutableList()
    internal val hasParameter = builder.parameters.isNotEmpty()

    /**
     * Contains some checks for the variable.
     * When a variable doesn't pass the check an exception will be thrown.
     */
    init {
        check(name.trim().isNotEmpty()) { "The name of a EnumProperty can't be empty" }
        if (generic != null && hasGeneric) {
            check(generic.trim().isNotEmpty()) { "The generic cast can't be empty" }
        }
    }


    internal fun write(
        codeWriter: CodeWriter
    ) {
        EnumPropertyWriter().write(this, codeWriter)
    }

    /**
     * Creates a string representation from the spec object.
     * @return the created string
     */
    override fun toString() = buildCodeString {
        write(
            this,
        )
    }

    /**
     * Converts a [EnumPropertySpec] to a [EnumPropertyWriter] instance.
     * @return the created instance
     */
    fun toBuilder(): EnumPropertyBuilder {
        val builder = EnumPropertyBuilder(name)
        builder.genericValueCast = generic
        builder.parameters += parameters
        return builder
    }

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
