package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.NEW_LINE

class ConstructorWriter {

    private val functionWriter = FunctionWriter()

    fun emit(spec: ConstructorSpec, codeWriter: CodeWriter) {
        for (modifier in spec.modifiers) {
            codeWriter.emit("${modifier.identifier}·")
        }
        if (spec.isFactory) {
            codeWriter.emit("${DartModifier.FACTORY.identifier}·")
        }


        codeWriter.emit(spec.name)

        if (spec.isNamed) {
            codeWriter.emit(".${spec.named}")
            spec.parameters.emitParameters(codeWriter) {
                it.write(codeWriter)
            }
            if (spec.isLambda) {
                codeWriter.emit("·=>$NEW_LINE")
                codeWriter.indent(2)
                codeWriter.emitCode(spec.body.build(), ensureTrailingNewline = true)
                codeWriter.unindent(2)
            }
        } else {
            codeWriter.emitCode("({$NEW_LINE")
            codeWriter.indent()

            spec.parameters.emitParameters(codeWriter, emitBrackets = false, ) {
                it.write(codeWriter)
            }

            codeWriter.unindent()
            codeWriter.emit(NEW_LINE)
            codeWriter.emit("}) = _${spec.name};")
        }
    }
}