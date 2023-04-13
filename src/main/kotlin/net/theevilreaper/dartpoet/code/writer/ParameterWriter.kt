package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import net.theevilreaper.dartpoet.util.SPACE

class ParameterWriter {

    fun write(spec: DartParameterSpec, codeWriter: CodeWriter) {
        if (spec.annotations.isNotEmpty()) {

        }

        for (modifier in spec.modifiers) {
            codeWriter.emit(modifier.identifier)
            codeWriter.emit(SPACE)
        }

    }
}