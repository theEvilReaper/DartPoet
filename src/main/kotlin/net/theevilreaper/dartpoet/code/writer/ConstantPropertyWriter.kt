package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier.*
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE

internal class ConstantPropertyWriter {

    fun emit(spec: ConstantPropertySpec, codeWriter: CodeWriter) {
        val modifiersAsString = spec.modifiers.joinToString(separator = SPACE, postfix = SPACE) { it.identifier }

        codeWriter.emit(modifiersAsString)

        if (spec.typeName != null) {
            codeWriter.emitCode("%T·", spec.typeName)
        }

        if (spec.isPrivat) {
            codeWriter.emit(PRIVATE.identifier)
        }

        codeWriter.emitCode("%L", spec.name)

        codeWriter.emit("·=·")
        codeWriter.emitCode(spec.initializer.build(), isConstantContext = true)
        codeWriter.emit(SEMICOLON)
    }
}
