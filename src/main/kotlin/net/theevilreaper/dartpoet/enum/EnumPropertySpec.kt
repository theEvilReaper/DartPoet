package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.EnumPropertyWriter
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet
import java.lang.reflect.Type
import kotlin.reflect.KClass

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
        WriterHelper.enumPropertyWriter.write(this, codeWriter)
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
        val builder = EnumPropertyBuilder(this.name, this.generic)
        builder.annotations.addAll(this.annotations)
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

        /**
         * Creates a new instance from the [EnumPropertyBuilder] to construct a new property.
         * @param name the name for the property
         * @param genericCast the generic cast for the property as [TypeName]
         * @return the created instance from the [EnumPropertyBuilder]
         */
        @JvmStatic
        fun builder(name: String, genericCast: TypeName) = EnumPropertyBuilder(name, genericCast)

        /**
         * Creates a new instance from the [EnumPropertyBuilder] to construct a new property.
         * @param name the name for the property
         * @param genericCast the generic cast for the property as [ClassName]
         * @return the created instance from the [EnumPropertyBuilder]
         */
        @JvmStatic
        fun builder(name: String, genericCast: ClassName) = EnumPropertyBuilder(name, genericCast)

        /**
         * Creates a new instance from the [EnumPropertyBuilder] to construct a new property.
         * @param name the name for the property
         * @param genericCast the generic cast for the property as [KClass]
         * @return the created instance from the [EnumPropertyBuilder]
         */
        @JvmStatic
        fun builder(name: String, genericCast: KClass<*>) = EnumPropertyBuilder(name, genericCast.asClassName())

        /**
         * Creates a new instance from the [EnumPropertyBuilder] to construct a new property.
         * @param name the name for the property
         * @param genericCast the generic cast for the property as [Type]
         * @return the created instance from the [EnumPropertyBuilder]
         */
        @JvmStatic
        fun builder(name: String, genericCast: Type) = EnumPropertyBuilder(name, genericCast.asTypeName())
    }
}
