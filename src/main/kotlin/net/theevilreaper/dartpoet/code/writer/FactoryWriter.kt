package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.InitializerAppender
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.function.factory.FactorySpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.ROUND_CLOSE
import net.theevilreaper.dartpoet.util.ROUND_OPEN
import net.theevilreaper.dartpoet.util.toImmutableList

internal class FactoryWriter : InitializerAppender<FactorySpec>, DocumentationAppender {

    fun write(spec: FactorySpec, codeWriter: CodeWriter) {
        emitDocumentation(spec.documentation, codeWriter)
        if (spec.isConst) {
            codeWriter.emitCode("%L·", DartModifier.CONST.identifier)
        }
        codeWriter.emitCode("%L·%T", DartModifier.FACTORY.identifier, spec.typeName)

        if (spec.hasNamedData) {
            codeWriter.emitCode(".%L", spec.named!!)
        }

        if (spec.named.orEmpty().trim().isNotEmpty()) {
            codeWriter.emit(ROUND_OPEN)
            spec.parameters.emitParameters(codeWriter)
            codeWriter.emitCode(ROUND_CLOSE)
        } else {
            writeParameters(spec, codeWriter)
        }

        val emitLambda = if (spec.withLambda) "·=>·" else "·{\n"
        codeWriter.emit(emitLambda)

        if (spec.initializerBlock.isNotEmpty()) {
            codeWriter.emitCode(spec.initializerBlock)
        }
    }

    private fun writeParameters(spec: FactorySpec, codeWriter: CodeWriter) {
        if (!spec.hasParameters) {
            codeWriter.emit("()")
            return
        }
        codeWriter.emit("(")
        spec.normalParameter.emitParameters(codeWriter, forceNewLines = false)

        if (spec.normalParameter.isNotEmpty() && (spec.hasAdditionalParameters || spec.parametersWithDefaults.isNotEmpty())) {
            codeWriter.emit(", ")
        }

        if (spec.hasAdditionalParameters) {
            emitRequiredAndNamedParameter(spec, codeWriter)
        }

        if (spec.parametersWithDefaults.isNotEmpty()) {
            codeWriter.emit("[")
            spec.parametersWithDefaults.emitParameters(codeWriter, forceNewLines = false)
            codeWriter.emit("]")
        }

        codeWriter.emit(")")
    }

    private fun emitRequiredAndNamedParameter(spec: FactorySpec, codeWriter: CodeWriter) {
        codeWriter.emit("$CURLY_OPEN")

        val namedRequired = spec.namedParameter.filter { it.isRequired && !it.hasInitializer }.toImmutableList()

        writeParameters(namedRequired, spec.normalParameter.isNotEmpty(), codeWriter)
        writeParameters(spec.requiredParameter, namedRequired.isNotEmpty(), codeWriter)

        val test =
            spec.namedParameter.minus(namedRequired).filter { it.isNullable || it.hasInitializer }.toImmutableList()

        writeParameters(test, spec.requiredParameter.isNotEmpty() || namedRequired.isNotEmpty(), codeWriter)

        codeWriter.emit("$CURLY_CLOSE")
    }

    private fun writeParameters(
        parameters: List<ParameterSpec>,
        emitSpaceComma: Boolean = false,
        codeWriter: CodeWriter
    ) {
        if (parameters.isEmpty()) return
        if (emitSpaceComma) {
            codeWriter.emit(", ")
        }
        parameters.emitParameters(codeWriter, forceNewLines = false)
    }
}
