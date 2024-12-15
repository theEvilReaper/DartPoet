package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.emitEnumParameterSpecs
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR

/**
 * The class contains the logic to transform a [EnumEntrySpec] into code.
 * Each generated part will be applied to a [CodeWriter] reference.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
internal class EnumEntryWriter : Writeable<EnumEntrySpec> {

    /**
     * Writes the given data from a [EnumEntrySpec] to a [CodeWriter] instance.
     * @param spec the [EnumEntrySpec] to get the data to write
     * @param writer the [CodeWriter] instance to apply the code
     */
    override fun write(spec: EnumEntrySpec, writer: CodeWriter) {
        spec.annotations.emitAnnotations(writer, inLineAnnotations = false)
        writer.emit(spec.name)
        if (spec.hasGeneric) {
            writer.emitCode("<%T>", spec.generic!!)
        }

        if (!spec.hasParameters) return
        writer.emit("(")
        spec.normalParameters.emitEnumParameterSpecs(writer)
        if (spec.normalParameters.isNotEmpty() && (spec.hasAdditionalParameters || spec.parametersWithDefaults.isNotEmpty())) {
            writer.emit(COMMA_SEPARATOR)
        }

        if (spec.hasAdditionalParameters) {
            spec.requiredParameters.emitEnumParameterSpecs(writer)
            if (spec.optionalNamed.isNotEmpty()) {
                writer.emit(COMMA_SEPARATOR)
            }
            spec.optionalNamed.emitEnumParameterSpecs(writer)
        }

        if (spec.parametersWithDefaults.isNotEmpty()) {
            spec.parametersWithDefaults.emitEnumParameterSpecs(writer)
        }

        writer.emit(")")
    }
}
