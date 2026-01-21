package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.EnumEntryWriter
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet
import net.theevilreaper.dartpoet.parameter.ParameterContext
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.util.ParameterFilter.filterParameter
import net.theevilreaper.dartpoet.util.ParameterHelper.excludeParameters

/**
 * The [EnumEntrySpec] represents a single entry in an enum class.
 * An entry can have a generic type, parameters and annotations.
 * Each parameter is wrapped in a [EnumParameterSpec] which contains the data about the parameter.
 * Dart allows the enum entry can have multiple parameters for an entry.
 * @since 1.0.0
 * @author theEvilReaper
 */
class EnumEntrySpec internal constructor(
    val builder: EnumEntryBuilder
) : ParameterContext<EnumParameterSpec> by ParameterContext(builder.parameters) {
    internal val name = builder.name
    internal val generic = builder.genericValueCast
    internal val hasGeneric = builder.genericValueCast != null
    internal val annotations = builder.annotations.toImmutableSet()

    /**
     * Contains some checks for the variable.
     * When a variable doesn't pass the check an exception will be thrown.
     */
    init {
        check(name.trim().isNotEmpty()) { "The name of a EnumProperty can't be empty" }
    }

    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.enumEntryWriter.write(this, codeWriter)
    }

    /**
     * Creates a string representation from the spec object.
     * @return the created string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Converts a [EnumEntrySpec] to a [EnumEntryWriter] instance.
     * @return the created instance
     */
    fun toBuilder(): EnumEntryBuilder {
        val builder = EnumEntryBuilder(this.name)
        builder.annotations.addAll(this.annotations)
        builder.genericValueCast = this.generic
        builder.parameters.addAll(this.parameters)
        return builder
    }

    /**
     * The companion object contains some helper methods to create a new instance of a [EnumEntryBuilder].
     */
    companion object {

        /**
         * Creates a new instance from the [EnumEntryBuilder] to construct a new property.
         * @param name the name for the property
         * @return the created instance from the [EnumEntryBuilder]
         */
        @JvmStatic
        fun builder(name: String) = EnumEntryBuilder(name)
    }
}
