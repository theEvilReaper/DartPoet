package net.theevilreaper.dartpoet.code.writer.typedef

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.function.typedef.alias.AliasTypeDefSpec

/**
 * The [TypeDefWriter] is the implementation of the [Writeable] interface for [AliasTypeDefSpec] structure.
 * It's responsible for writing the typedef structure to the given [CodeWriter].
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
class TypeDefWriter : Writeable<AbstractTypeDef<*>> {

    /**
     * Appends the [AliasTypeDefSpec] to the given [CodeWriter].
     * @param spec the spec to write
     * @param writer the writer to append the data
     */
    override fun write(spec: AbstractTypeDef<*>, writer: CodeWriter) {
        writer.emit(DartModifier.TYPEDEF.identifier)
        writer.emitSpace()
        writer.emitCode("%T", spec.type)
        writer.emitCode(" = ")
        spec.writeRightHandSide(writer)
    }
}