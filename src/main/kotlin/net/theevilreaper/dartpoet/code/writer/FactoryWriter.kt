package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.InitializerAppender
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.constructor.ConstructorDelegation
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.parameter.ParameterData

internal class FactoryWriter : InitializerAppender<FactorySpec>, DocumentationAppender {

    fun write(spec: FactorySpec, codeWriter: CodeWriter) {
        emitDocumentation(spec.documentation, codeWriter)
        emitAnnotations(spec, codeWriter)
        if (spec.isConst) {
            codeWriter.emit(DartModifier.CONST.identifier)
            codeWriter.emitSpace()
        }
        codeWriter.emitCode("%L·%T", DartModifier.FACTORY.identifier, spec.typeName)

        if (spec.hasNamedData) {
            codeWriter.emitCode(".%L", spec.named!!)
        }

        if (!spec.hasParameters) {
            writeSimpleConstructor(spec, codeWriter)
            return
        }

        val parameterData: ParameterData = ParameterData.fromFactory(spec)
        ParameterHelper.writeParameters(parameterData, codeWriter, indent = true, filterExcluded = false)

        ConstructorDelegation.appendDelegation(spec.constructorDelegation, spec.initializerBlock, codeWriter)
    }

    private fun emitAnnotations(spec: FactorySpec, codeWriter: CodeWriter) {
        if (spec.annotations.isEmpty()) return
        spec.annotations.emitAnnotations(codeWriter)
    }

    private fun writeSimpleConstructor(spec: FactorySpec, codeWriter: CodeWriter) {
        codeWriter.emit("()")
        ConstructorDelegation.appendDelegation(spec.constructorDelegation, spec.initializerBlock, codeWriter)
    }
}
