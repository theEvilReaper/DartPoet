package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.SPACE

class ParameterWriter {

    fun write(spec: DartParameterSpec, codeWriter: CodeWriter) {
        codeWriter.emit("${spec.type} ${spec.name}·")

        if (spec.initializer.isNotEmpty()) {
            codeWriter.emit("=·")
            codeWriter.emitCode(spec.initializer.build())
        }

    }
}