package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.NEW_LINE

class FactoryMethodBuilder {

    private val parameterWriter: ParameterWriter = ParameterWriter()

    fun write(functionSpec: ConstructorSpec, codeWriter: CodeWriter) {
        codeWriter.emit("${DartModifier.FACTORY.identifier}·")
        codeWriter.emit("${functionSpec.name}.")
        codeWriter.emit("${functionSpec.named}")
        codeWriter.emit("(")
        if (functionSpec.parameters.isNotEmpty()) {
            functionSpec.parameters.forEach {
                parameterWriter.write(it, codeWriter)
            }
        }

        codeWriter.emit(")·")
        if (functionSpec.isLambda) {
            codeWriter.emit("=>$NEW_LINE")
            codeWriter.indent(2)
            codeWriter.emitCode(functionSpec.body.build(), ensureTrailingNewline = true)
            codeWriter.unindent(2)
        } else {
            codeWriter.emit("{")
            codeWriter.indent()
            codeWriter.emitCode(functionSpec.body.build(), ensureTrailingNewline = true)
            codeWriter.unindent()
            codeWriter.emit("}")
        }
    }
}