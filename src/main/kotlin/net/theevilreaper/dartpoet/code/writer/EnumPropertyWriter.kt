package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.enum.EnumPropertySpec

class EnumPropertyWriter {
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
