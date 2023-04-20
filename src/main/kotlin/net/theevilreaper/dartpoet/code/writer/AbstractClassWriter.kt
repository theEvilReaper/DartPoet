package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.NEW_LINE

class AbstractClassWriter {

    private val functionWriter = FunctionWriter()

    fun write(spec: DartClassSpec, writer: CodeWriter) {
        for (modifier in spec.modifiers) {
            writer.emit("${modifier.identifier}·")
        }
        writer.emit("${spec.name!!.trim()}{", nonWrapping = true)
        writer.indent()

        spec.functions.emitFunctions(writer) {
            functionWriter.emit(it, writer)
        }

        writer.unindent()

        writer.emit(CURLY_CLOSE.toString())

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }
}