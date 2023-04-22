package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.property.DartPropertySpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE

class PropertyWriter {

    fun write(property: DartPropertySpec, writer: CodeWriter) {
        property.annotations.emitAnnotations(writer) {
            it.write(writer, inline = false)
        }

        for (modifier in property.modifiers) {
            writer.emit(modifier.identifier)
            writer.emit(SPACE)
        }

        writer.emit(property.type)

        if (property.nullable) {
            writer.emit("? ")
        } else {
            writer.emit(SPACE)
        }

        writer.emit(if (property.isPrivate) DartModifier.PRIVATE.identifier else EMPTY_STRING)
        writer.emit(property.name)

        if (property.initBlock.isNotEmpty()) {
            writer.emit("·=·")
            writer.emitCode(property.initBlock.build(), isConstantContext = true)
        }
        writer.emit(SEMICOLON)
    }
}