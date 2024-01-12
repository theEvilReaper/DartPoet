package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.InitializerAppender
import net.theevilreaper.dartpoet.code.VariableAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE

internal class ConstantPropertyWriter : Writeable<ConstantPropertySpec>, InitializerAppender<PropertyWriter>, VariableAppender {

    override fun write(spec: ConstantPropertySpec, writer: CodeWriter) {
        val modifiersAsString = spec.modifiers.joinToString(separator = SPACE, postfix = SPACE) { it.identifier }

        writer.emit(modifiersAsString)

        if (spec.typeName != null) {
            writer.emitCode("%T·", spec.typeName)
        }

        writer.emitCode("%L", ensureVariableNameWithPrivateModifier(spec.isPrivate, spec.name))
        writeInitBlock(spec.initializer.build(), writer)
        writer.emit(SEMICOLON)
    }
}
