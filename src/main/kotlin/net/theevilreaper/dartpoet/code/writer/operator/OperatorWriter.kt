package net.theevilreaper.dartpoet.code.writer.operator

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.operator.DartOperatorSpec
import net.theevilreaper.dartpoet.util.SEMICOLON

internal class OperatorWriter : Writeable<DartOperatorSpec>, DocumentationAppender {

    override fun write(
        spec: DartOperatorSpec,
        writer: CodeWriter
    ) {
        emitDocumentation(spec.docs, writer)
        spec.annotations.emitAnnotations(codeWriter = writer)

        writer.emitCode("%T", spec.returnType)
        writer.emitSpace()
        writer.emitCode("%L", DartModifier.OPERATOR.identifier)
        writer.emitSpace()
        writer.emitCode("%L", spec.operator.symbol)

        writeParameters(spec, writer)
        writeBody(spec, writer)
    }

    private fun writeParameters(spec: DartOperatorSpec, writer: CodeWriter) {
        if (!spec.hasParameters) {
            writer.emitEmptyRoundBrackets()
            return
        }

        writer.emit("(")
        spec.parameters.forEachIndexed { index, parameter ->
            parameter.write(writer)
            if (index < spec.parameters.size - 1) {
                writer.emit(", ")
            }
        }
        writer.emit(")")
    }

    private fun writeBody(spec: DartOperatorSpec, writer: CodeWriter) {
        if (spec.body.isEmpty()) {
            writer.emit(SEMICOLON)
            return
        }

        when (spec.type) {
            FunctionType.STANDARD -> {
                writer.emitSpace()
                writer.emit("{\n")
                writer.indent()
                writer.emitCode(spec.body, ensureTrailingNewline = false)
                writer.unindent()
                writer.emit("\n}")
            }

            FunctionType.SHORTEN -> {
                writer.emitSpace()
                writer.emit(FunctionType.SHORTEN.identifier)
                writer.emitSpace()
                writer.emitCode(spec.body, ensureTrailingNewline = false)
            }
        }
    }
}
