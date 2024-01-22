package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.InitializerAppender
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.factory.FactorySpec
import net.theevilreaper.dartpoet.util.toImmutableList

internal class FactoryWriter : InitializerAppender<FactorySpec>, DocumentationAppender {

    fun write(spec: FactorySpec, codeWriter: CodeWriter) {
        emitDocumentation(spec.documentation, codeWriter)
        codeWriter.emitCode("%L·%T", DartModifier.FACTORY.identifier, spec.typeName)

        if (spec.hasNamedData) {
            codeWriter.emitCode(".%L", spec.named!!)
        }

        if (spec.hasParameter) {
            codeWriter.emit("(")
            spec.parameters.toImmutableList().emitParameters(codeWriter)
            codeWriter.emit(")")
        } else {
            codeWriter.emit("()")
        }

        val emitLambda = if (spec.withLambda) "·=>·" else "·{\n"
        codeWriter.emit(emitLambda)

        if (spec.initializerBlock.isNotEmpty()) {
            codeWriter.emitCode(spec.initializerBlock)
        }
    }
}
