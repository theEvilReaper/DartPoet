package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.typedef.TypeDefSpec
import net.theevilreaper.dartpoet.util.SEMICOLON

class TypeDefWriter : Writeable<TypeDefSpec> {
    override fun write(spec: TypeDefSpec, writer: CodeWriter) {
        writer.emit("${DartModifier.TYPEDEF.identifier}·${spec.typeDefName}")
        if (spec.typeCast != null) {
            writer.emitCode("<%T>", spec.typeCast)
        }
        writer.emit("·=·")
        writer.emitCode("%T", spec.returnType)

        if (spec.name != null) {
            writer.emitCode("·%L", spec.name)
        }

        if (spec.hasParameters) {
            writer.emit("(")
            spec.parameters.emitParameters(writer, forceNewLines = false, emitBrackets = false, emitSpace = spec.parameters.size > 1)
            writer.emit(")")
        }
        writer.emit(SEMICOLON)
    }
}
