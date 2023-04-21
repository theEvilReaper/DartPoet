package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.util.NEW_LINE

class ExtensionWriter {

    fun write(spec: ExtensionSpec, writer: CodeWriter) {
        writer.emit("${DartModifier.EXTENSION.identifier}·")
        writer.emit("${spec.name}·")
        writer.emit("${DartModifier.ON.identifier}·")
        writer.emit("${spec.extClass}·")
        writer.emit("{\n")
        writer.indent()

        spec.functions.emitFunctions(writer) {
            it.write(writer)
        }

        writer.unindent()
        writer.emit("}")

        if (spec.endWithNewLine) {
            writer.emit(NEW_LINE)
        }

    }

}