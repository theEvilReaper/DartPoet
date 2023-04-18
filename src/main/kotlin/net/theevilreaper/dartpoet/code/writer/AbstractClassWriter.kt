package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.NEW_LINE

class AbstractClassWriter {

    private val functionWriter = FunctionWriter()

    fun write(spec: DartClassSpec, writer: CodeWriter) {
        for (modifier in spec.modifiers) {
            writer.emit("${modifier.identifier}")
        }
        writer.emit("${spec.name!!.trim()}{", nonWrapping = true)
        writer.indent()

        if (spec.functions.isNotEmpty()) {
            spec.functions.forEach {
                functionWriter.emit(it as DartFunctionSpec, writer)
            }
        }

        writer.unindent()

        writer.emit(CURLY_CLOSE.toString())

        if (spec.endsWithNewLine) {
            writer.emit(NEW_LINE)
        }
    }
}