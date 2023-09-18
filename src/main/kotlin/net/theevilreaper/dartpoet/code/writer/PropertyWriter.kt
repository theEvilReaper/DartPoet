package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
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

        val modifierString = property.modifiers.joinToString(separator = SPACE) { it.identifier }
        writer.emit(modifierString)

        if (!property.isConst) {
            if (property.modifiers.isNotEmpty()) {
                writer.emit(SPACE)
            }
            writer.emitCode("%T", property.type)
        }

        writer.emit("·")

        writer.emit(if (property.isPrivate) DartModifier.PRIVATE.identifier else EMPTY_STRING)
        writer.emit(property.name)
        emitInitBlock(property.initBlock, writer)
        writer.emit(SEMICOLON)
    }

    private fun emitInitBlock(initBlock: CodeBlock.Builder, writer: CodeWriter) {
        if (initBlock.isEmpty()) return
        writer.emit("·=·")
        writer.emitCode(initBlock.build(), isConstantContext = true)
    }
}