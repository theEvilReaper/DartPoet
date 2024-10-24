package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.StringHelper
import net.theevilreaper.dartpoet.util.parameter.ParameterData

/**
 * The [TypeDefWriter] is the implementation of the [Writeable] interface for [TypeDefSpec] structure.
 * It's responsible for writing the typedef structure to the given [CodeWriter].
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
class TypeDefWriter : Writeable<TypeDefSpec> {

    /**
     * Appends the [TypeDefSpec] to the given [CodeWriter].
     * @param spec the spec to write
     * @param writer the writer to append the data
     */
    override fun write(spec: TypeDefSpec, writer: CodeWriter) {
        writer.emit(DartModifier.TYPEDEF.identifier)
        writer.emitSpace()
        writer.emit(spec.typeDefName)
        if (spec.typeCasts.isNotEmpty()) {
            val typeIterable: Iterable<TypeName> = spec.typeCasts.filterNotNull().asIterable()
            val typeCasts = StringHelper.concatData(typeIterable, separator = COMMA_SEPARATOR) { it.toString() }
            writer.emitCode("<%L>", typeCasts)
        }
        writer.emitSpace()
        writer.emit("=")
        writer.emitSpace()
        writer.emitCode("%T", spec.returnType)

        if (spec.name != null) {
            writer.emitSpace()
            writer.emit(spec.name)
        }

        val parameterData: ParameterData<ParameterSpec> = ParameterData.fromTypeDef(spec)

        if (parameterData.hasParameters) {
            ParameterHelper.writeParameters(parameterData, writer, indent = parameterData.requiredParameters.size > 1)
        }
        writer.emitCode(SEMICOLON)
    }
}
