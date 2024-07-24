package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.code.writer.ConstructorWriter
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.code.writer.TypeDefWriter
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.parameter.ParameterData
import org.jetbrains.annotations.ApiStatus

/**
 * The [ParameterHelper] includes a generalization of the write process for each parameter implementation.
 * It helps to reduce the complexity of the [FunctionWriter], [ConstructorWriter] and [TypeDefWriter] by providing a common way to write.
 * Before this change, each writer had its own implementation which was redundant and error-prone.
 * @since 1.0.0
 * @version 1.0.0
 * @author theEvilReaper
 */
@ApiStatus.Internal
internal object ParameterHelper {

    /**
     * Excludes the given [ParameterSpec] from the source list.
     * @param source the source list to exclude the parameters from
     * @param params the parameters to exclude
     * @return a new list without the given parameters
     */
    fun excludeParameters(source: List<ParameterSpec>, vararg params: List<ParameterSpec>): List<ParameterSpec> {
        if (params.isEmpty()) return source.toImmutableList()

        val parametersToRemove: Set<ParameterSpec> = params.flatMap { it }.toSet()
        return source.minus(parametersToRemove).toImmutableList()
    }

    /**
     * Writes each parameter which is part of the [ParameterData] to the given [CodeWriter].
     * @param data the data to write
     * @param codeWriter the writer to append the data
     * @param indent if the generated code should be indented
     * @param filterExcluded whether to filter the excluded parameters
     */
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

    /**
     * Emits the required and named parameters to the given [CodeWriter].
     * The mentioned parameters have their own method to reduce the complexity
     * @param data the data to write
     * @param codeWriter the writer to append the data
     * @param indent whether to indent the data
     * @param filterExcluded whether to filter the excluded parameters
     */
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
        val excludedNamedParameters = when (filterExcluded) {
            true -> excludeParameters(data.namedParameter, namedRequired).filter { it.isNullable || it.hasInitializer }
            false -> excludeParameters(data.namedParameter, namedRequired)
        }
        internalParameterWrite(namedRequired, namedRequired.isNotEmpty(), codeWriter)
        internalParameterWrite(data.requiredParameters, excludedNamedParameters.isNotEmpty(), codeWriter)
        internalParameterWrite(excludedNamedParameters, codeWriter = codeWriter)

        if (indent) {
            codeWriter.emit(NEW_LINE)
            codeWriter.unindent()
        }

        codeWriter.emit("$CURLY_CLOSE")
    }

    /**
     * Writes each [ParameterSpec] in the list to an active [CodeWriter].
     * @param parameters the list of parameters to write
     * @param trailingComma whether to emit a trailing comma
     * @param codeWriter the active [CodeWriter]
     */
    private fun internalParameterWrite(
        parameters: List<ParameterSpec>,
        trailingComma: Boolean = false,
        codeWriter: CodeWriter
    ) {
        if (parameters.isEmpty()) return

        parameters.emitParameters(codeWriter, forceNewLines = false)
        if (trailingComma) {
            codeWriter.emit(COMMA_SEPARATOR)
        }
    }
}
