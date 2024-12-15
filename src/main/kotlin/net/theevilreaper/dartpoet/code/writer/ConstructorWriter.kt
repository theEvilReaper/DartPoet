package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.parameter.ParameterData

internal class ConstructorWriter : Writeable<ConstructorSpec>, DocumentationAppender {

    override fun write(spec: ConstructorSpec, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)
        if (spec.modifiers.contains(DartModifier.CONST)) {
            writer.emit(DartModifier.CONST.identifier)
            writer.emitSpace()
        }

        writer.emit(spec.name)

        if (spec.isNamed) {
            writer.emit(".${spec.named}")
        }

        val parameterData: ParameterData<ParameterSpec> = ParameterData.fromConstructor(spec)
        ParameterHelper.writeParameters(parameterData, writer)

        if (spec.isLambda) {
            writer.emitSpace()
            writer.emit("=>$NEW_LINE")
            writer.indent(2)
            writer.emitCode(spec.initializer.build(), ensureTrailingNewline = true)
            writer.unindent(2)
        } else {
            if (spec.initializer.isNotEmpty()) {
                writer.emit(":")
                writer.emitSpace()
                writer.emitCode(spec.initializer.build(), ensureTrailingNewline = false)
                writer.emit(SEMICOLON)
                return
            }

            writer.emit(SEMICOLON)
        }
    }
}
