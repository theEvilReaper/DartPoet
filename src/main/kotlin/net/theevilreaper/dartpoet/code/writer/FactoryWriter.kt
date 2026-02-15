package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.InitializerAppender
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.constructor.ConstructorDelegation
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.parameter.ParameterData

/**
 * Writes the factory to the given [CodeWriter].
 *
 * @author theEvilReaper
 * @since 1.0.0
 */
internal class FactoryWriter : InitializerAppender<FactorySpec>, DocumentationAppender {

    /**
     * Writes the given data from a [FactorySpec] to the [CodeWriter].
     * @param spec the factory spec
     * @param codeWriter the code writer
     */
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

        val parameterData: ParameterData<ParameterSpec> = ParameterData.of(spec)
        ParameterHelper.writeParameters(parameterData, codeWriter, indent = true)

        ConstructorDelegation.appendDelegation(spec.constructorDelegation, spec.initializerBlock, codeWriter)
    }

    /**
     * Emits the annotations for the factory.
     * @param spec the factory spec
     * @param codeWriter the code writer
     */
    private fun emitAnnotations(spec: FactorySpec, codeWriter: CodeWriter) {
        if (spec.annotations.isEmpty()) return
        spec.annotations.emitAnnotations(codeWriter)
    }

    /**
     * Writes a simple constructor without any parameters.
     * @param spec the factory spec
     * @param codeWriter the code writer
     */
    private fun writeSimpleConstructor(spec: FactorySpec, codeWriter: CodeWriter) {
        codeWriter.emitEmptyRoundBrackets()
        ConstructorDelegation.appendDelegation(spec.constructorDelegation, spec.initializerBlock, codeWriter)
    }
}
