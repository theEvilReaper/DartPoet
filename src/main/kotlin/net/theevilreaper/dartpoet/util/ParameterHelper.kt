package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.parameter.ParameterData
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object ParameterHelper {

    fun excludeParameters(source: List<ParameterSpec>, vararg params: List<ParameterSpec>): List<ParameterSpec> {
        if (params.isEmpty()) return source.toImmutableList()

        val parametersToRemove: Set<ParameterSpec> = params.flatMap { it }.toSet()
        return source.minus(parametersToRemove).toImmutableList()
    }

    fun writeParameters(
        data: ParameterData,
        codeWriter: CodeWriter,
        indent: Boolean = false,
        filterExcluded: Boolean = true
    ) {
        if (!data.hasParameters) {
            codeWriter.emit("()")
            return
        }
        codeWriter.emit("(")
        data.normalParameters.emitParameters(codeWriter, forceNewLines = false)

        if (data.normalParameters.isNotEmpty() && (data.hasAdditionalParameters || data.parametersWithDefaults.isNotEmpty())) {
            codeWriter.emit(", ")
        }

        if (data.hasAdditionalParameters) {
            emitRequiredAndNamedParameter(data, codeWriter, indent, filterExcluded)
        }

        if (data.parametersWithDefaults.isNotEmpty()) {
            codeWriter.emit("[")
            data.parametersWithDefaults.emitParameters(codeWriter, forceNewLines = false)
            codeWriter.emit("]")
        }

        codeWriter.emit(")")
    }

    private fun emitRequiredAndNamedParameter(
        data: ParameterData,
        codeWriter: CodeWriter,
        indent: Boolean = false,
        filterExcluded: Boolean = true
    ) {
        codeWriter.emit("$CURLY_OPEN")

        if (indent) {
            codeWriter.emit(NEW_LINE)
            codeWriter.indent()
        }

        val namedRequired = data.namedParameter.filter { it.isRequired && !it.hasInitializer }.toImmutableList()
        writeParameters(namedRequired, data.normalParameters.isNotEmpty(), codeWriter)
        writeParameters(data.requiredParameters, namedRequired.isNotEmpty(), codeWriter)
        val excludedNamedParameters = when (filterExcluded) {
            true -> excludeParameters(data.namedParameter, namedRequired).filter { it.isNullable || it.hasInitializer }
            false -> excludeParameters(data.namedParameter, namedRequired)
        }
        val emitSpace: Boolean = data.requiredParameters.isNotEmpty() || namedRequired.isNotEmpty()
        writeParameters(excludedNamedParameters, emitSpace, codeWriter)

        if (indent) {
            codeWriter.emit(NEW_LINE)
            codeWriter.unindent()
        }

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