package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.ROUND_CLOSE
import net.theevilreaper.dartpoet.util.ROUND_OPEN

/**
 * The class contains the logic to transform a [EnumPropertySpec] into code.
 * Each generated part will be applied to a [CodeWriter] reference.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
internal class EnumPropertyWriter : Writeable<EnumPropertySpec> {

    /**
     * Writes the given data from a [EnumPropertySpec] to a [CodeWriter] instance.
     * @param spec the [EnumPropertySpec] to get the data to write
     * @param writer the [CodeWriter] instance to apply the code
     */
    override fun write(spec: EnumPropertySpec, writer: CodeWriter) {
        spec.annotations.emitAnnotations(codeWriter = writer, inLineAnnotations = false)
        writer.emit(spec.name)
        if (spec.hasGeneric) {
            writer.emitCode("<%T>", spec.generic!!)
        }

        if (spec.hasParameter) {
            writer.emit(ROUND_OPEN)
            spec.parameters.forEachIndexed { index, codeBlock ->
                if (index > 0) {
                    writer.emit(COMMA_SEPARATOR)
                }
                writer.emitCode(codeBlock, isConstantContext = false, ensureTrailingNewline = false)
            }
            writer.emit(ROUND_CLOSE)
        }
    }
}
