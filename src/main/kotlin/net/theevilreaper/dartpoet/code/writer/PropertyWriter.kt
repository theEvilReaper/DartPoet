package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE

class PropertyWriter {

    fun write(property: PropertySpec, writer: CodeWriter) {
        if (property.hasDocs) {
            property.docs.forEach { writer.emitDoc(it) }
        }
        property.annotations.emitAnnotations(writer) {
            it.write(writer, inline = false)
        }

        property.modifiers.forEachIndexed { index, modifier ->
            if (index > 0) {
                writer.emit(SPACE)
            }
            writer.emit(modifier.identifier)
        }

        if (!property.isConst) {
            if (property.modifiers.isNotEmpty()) {
                writer.emit(SPACE)
            }
            writer.emit(property.type)
        }

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