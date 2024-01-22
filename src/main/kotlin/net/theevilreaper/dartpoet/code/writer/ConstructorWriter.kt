package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.SEMICOLON

internal class ConstructorWriter : Writeable<ConstructorSpec>, DocumentationAppender {

    override fun write(spec: ConstructorSpec, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)
        if (spec.modifiers.contains(DartModifier.CONST)) {
            writer.emit("${DartModifier.CONST.identifier}·")
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

        spec.parameters.emitParameters(writer)

        if (spec.hasNamedParameters) {
            if (spec.parameters.isNotEmpty()) {
                writer.emit(",$NEW_LINE")
            }
            writer.emit("$CURLY_OPEN")
            writer.emit(NEW_LINE)
            writer.indent()

            spec.requiredAndNamedParameters.emitParameters(writer, emitSpace = false, forceNewLines = true) {
                it.write(writer)
            }

            writer.unindent()
            writer.emit(NEW_LINE)
            writer.emit("$CURLY_CLOSE")
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
                writer.emit(SEMICOLON)
                return
            }

            writer.emit(SEMICOLON)
        }
    }
}
