package net.theevilreaper.dartpoet.code.writer.typedef

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.function.typedef.FunctionTypeDefSpec
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.StringHelper

/**
 * The [TypeDefWriter] is the implementation of the [net.theevilreaper.dartpoet.code.Writeable] interface for [net.theevilreaper.dartpoet.function.typedef.TypeDefSpec] structure.
 * It's responsible for writing the typedef structure to the given [net.theevilreaper.dartpoet.code.CodeWriter].
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
class TypeDefWriter : Writeable<TypeDefSpec> {

    /**
     * Appends the [TypeDefSpec] to the given [net.theevilreaper.dartpoet.code.CodeWriter].
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
            writer.emitGenericBlock("%L", typeCasts)
        }
        writer.emitSpace()
        writer.emit("=")
        writer.emitSpace()
        writer.emitCode("%T", spec.returnType)

        if (spec is FunctionTypeDefSpec) {
            WriterHelper.functionTypeDefWriter.write(spec, writer)
        }
        writer.emitCode(SEMICOLON)
    }
}