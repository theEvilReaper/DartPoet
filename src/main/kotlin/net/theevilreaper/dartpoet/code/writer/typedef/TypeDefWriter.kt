package net.theevilreaper.dartpoet.code.writer.typedef

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.function.typedef.alias.TypeDefSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.StringHelper

/**
 * The [TypeDefWriter] is the implementation of the [Writeable] interface for [TypeDefSpec] structure.
 * It's responsible for writing the typedef structure to the given [CodeWriter].
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
class TypeDefWriter : Writeable<AbstractTypeDef<*>> {

    /**
     * Appends the [TypeDefSpec] to the given [CodeWriter].
     * @param spec the spec to write
     * @param writer the writer to append the data
     */
    override fun write(spec: AbstractTypeDef<*>, writer: CodeWriter) {
        writer.emit(DartModifier.TYPEDEF.identifier)
        writer.emitSpace()
        writer.emit(spec.name)
        if (spec.typeCasts.isNotEmpty()) {
            val typeIterable: Iterable<TypeName> = spec.typeCasts.filterNotNull().asIterable()
            val typeCasts = StringHelper.concatData(typeIterable, separator = COMMA_SEPARATOR) { it.toString() }
            writer.emitGenericBlock("%L", typeCasts)
        }
        writer.emitCode(" = ")
        spec.writeRightHandSide(writer)
    }
}