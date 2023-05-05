package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.NEW_LINE

@Deprecated(message = "Currently no usage for this class")
class FactoryMethodBuilder {

    fun write(functionSpec: ConstructorSpec, codeWriter: CodeWriter) {
        codeWriter.emit("${DartModifier.FACTORY.identifier}·")
        codeWriter.emit("${functionSpec.name}.")
        codeWriter.emit(functionSpec.named.orEmpty())
        codeWriter.emit("(")

        functionSpec.parameters.emitParameters(codeWriter) {
            it.write(codeWriter)
        }

        codeWriter.emit(")·")
        if (functionSpec.isLambda) {
            codeWriter.emit("=>$NEW_LINE")
            codeWriter.indent(2)
            codeWriter.emitCode(functionSpec.body.build(), ensureTrailingNewline = true)
            codeWriter.unindent(2)
        } else {
            codeWriter.emit("$CURLY_OPEN")
            codeWriter.indent()
            codeWriter.emitCode(functionSpec.body.build(), ensureTrailingNewline = true)
            codeWriter.unindent()
            codeWriter.emit("$CURLY_CLOSE")
        }
    }
}