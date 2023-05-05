package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.enum.EnumPropertySpec

/**
 * The class contains the logic to transform a [EnumPropertySpec] into code.
 * Each generated part will be applied to a [CodeWriter] reference.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
class EnumPropertyWriter {

    /**
     * Writes the given data from a [EnumPropertySpec] to a [CodeWriter] instance.
     * @param propertySpec the [EnumPropertySpec] to get the data to write
     * @param writer the [CodeWriter] instance to apply the code
     */
    fun write(propertySpec: EnumPropertySpec, writer: CodeWriter) {
        writer.emit(propertySpec.name)
        if (propertySpec.hasGeneric) {
            writer.emit("<")
            writer.emit(propertySpec.generic!!)
            writer.emit(">")
        }

        if (propertySpec.hasParameter) {
            writer.emit("(")
            propertySpec.parameters.forEach {
                writer.emitCode(it, false, false)
            }
            writer.emit(")")
        }
    }
}
