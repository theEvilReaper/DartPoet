package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.constructor.factory.FactorySpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object ParameterHelper {

    fun excludeParameters(source: List<ParameterSpec>, vararg params: List<ParameterSpec>): List<ParameterSpec> {
        if (params.isEmpty()) return source.toImmutableList()

        val parametersToRemove: Set<ParameterSpec> = params.flatMap { it }.toSet()
        return source.minus(parametersToRemove).toImmutableList()
    }

    fun writeParameters(
        spec: FunctionSpec,
        codeWriter: CodeWriter,
    ) {
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
            emitRequiredAndNamedParameter(
                namedParameter = spec.namedParameter,
                normalParameters = spec.normalParameter,
                requiredParameters = spec.requiredParameter,
                codeWriter = codeWriter
            )
        }

        if (spec.parametersWithDefaults.isNotEmpty()) {
            codeWriter.emit("[")
            spec.parametersWithDefaults.emitParameters(codeWriter, forceNewLines = false)
            codeWriter.emit("]")
        }

        codeWriter.emit(")")
    }

    private fun emitRequiredAndNamedParameter(spec: FunctionSpec, codeWriter: CodeWriter) {
        codeWriter.emit("$CURLY_OPEN")

        val namedRequired = spec.namedParameter.filter { it.isRequired && !it.hasInitializer }.toImmutableList()
        writeParameters(namedRequired, spec.normalParameter.isNotEmpty(), codeWriter)
        writeParameters(spec.requiredParameter, namedRequired.isNotEmpty(), codeWriter)
        val namedParameter =
            excludeParameters(spec.namedParameter, namedRequired).filter { it.isNullable || it.hasInitializer }
        val emitSpace: Boolean = spec.requiredParameter.isNotEmpty() || namedRequired.isNotEmpty()
        writeParameters(namedParameter, emitSpace, codeWriter)
        codeWriter.emit("$CURLY_CLOSE")
    }

    private fun emitRequiredAndNamedParameter(
        namedParameter: List<ParameterSpec>,
        normalParameters: List<ParameterSpec>,
        requiredParameters: List<ParameterSpec>,
        codeWriter: CodeWriter
    ) {
        codeWriter.emit("$CURLY_OPEN")

        val namedRequired = namedParameter.filter { it.isRequired && !it.hasInitializer }.toImmutableList()
        writeParameters(namedRequired, normalParameters.isNotEmpty(), codeWriter)
        writeParameters(requiredParameters, namedRequired.isNotEmpty(), codeWriter)
        val excludedNamedParameters =
            excludeParameters(namedParameter, namedRequired).filter { it.isNullable || it.hasInitializer }
        val emitSpace: Boolean = requiredParameters.isNotEmpty() || namedRequired.isNotEmpty()
        writeParameters(excludedNamedParameters, emitSpace, codeWriter)
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