package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON

class ConstructorWriter {

    fun emit(spec: ConstructorSpec, writer: CodeWriter) {
        if (spec.modifiers.contains(DartModifier.CONST)) {
            writer.emit("${DartModifier.CONST.identifier}·")
        }

        if (spec.isFactory) {
            writer.emit("${DartModifier.FACTORY.identifier}·")
        }

        writer.emit(spec.name)

        if (spec.isNamed) {
            writer.emit(".${spec.named}")
        }

        if (!spec.hasParameters) {
            writer.emit("()$SEMICOLON")
            return
        }

        writer.emit("(")

        spec.parameters.emitParameters(writer, emitBrackets = false) {
            it.write(writer)
        }

        if (spec.hasNamedParameters) {
            writer.emit("{")
            writer.emit("\n")
            writer.indent(1)

            spec.requiredAndNamedParameters.emitParameters(writer, emitBrackets = false, emitSpace = false, forceNewLines = true) {
                it.write(writer)
            }

            writer.unindent(1)
            writer.emit(NEW_LINE)
            writer.emit("}")
        }


        writer.emit(")")

        if (spec.isLambda) {
            writer.emit("·=>$NEW_LINE")
            writer.indent(2)
            writer.emitCode(spec.initializer.build(), ensureTrailingNewline = true)
            writer.unindent(2)
        } else {

            if (spec.initializer.isNotEmpty()) {
                writer.emit(":·")
                writer.emitCode(spec.initializer.build(), ensureTrailingNewline = false)
            }

            if (spec.isFactory) {
                writer.emit(" = _${spec.name}")
            }

            writer.emit(SEMICOLON)
        }
    }
}
