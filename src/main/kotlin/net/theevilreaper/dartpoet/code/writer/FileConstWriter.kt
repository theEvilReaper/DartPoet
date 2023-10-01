package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.file.FileConstantSpec
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE

class FileConstWriter {

    fun emit(spec: FileConstantSpec, codeWriter: CodeWriter) {
        val modifiersAsString = spec.modifiers.joinToString(postfix = SPACE) { it.identifier }

        codeWriter.emit(modifiersAsString)

        if (spec.typeName != null) {
            codeWriter.emitCode("%T·", spec.typeName)
        }

        codeWriter.emitCode("%L", spec.name)

        codeWriter.emit("·=·")
        codeWriter.emitCode(spec.initializer.build(), isConstantContext = true)
        codeWriter.emit(SEMICOLON)
    }
}
