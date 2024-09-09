package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitMiniParameters
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.code.writer.ConstructorWriter
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.code.writer.TypeDefWriter
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.minimized.MinimizedParameter
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

    fun excludeMiniParameter(source: List<MinimizedParameter>, vararg params: List<MinimizedParameter>): List<MinimizedParameter> {
        if (params.isEmpty()) return source.toImmutableList()

        val parametersToRemove: Set<MinimizedParameter> = params.flatMap { it }.toSet()
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
        data: ParameterData<ParameterSpec>,
        codeWriter: CodeWriter,
        indent: Boolean = false,
    ) {
        if (!data.hasParameters) {
            codeWriter.emit("()")
            return
        }
        codeWriter.emit("(")
        data.positionalParameters.emitParameters(codeWriter, forceNewLines = false)

        if (data.positionalParameters.isNotEmpty() && (data.hasAdditionalParameters || data.optionalAndDefault.isNotEmpty())) {
            codeWriter.emit(", ")
        }

        if (data.hasAdditionalParameters) {
            emitRequiredAndNamedParameter(data, codeWriter, indent)
        }

        if (data.optionalAndDefault.isNotEmpty()) {
            codeWriter.emit("[")
            data.optionalAndDefault.emitParameters(codeWriter, forceNewLines = false)
            codeWriter.emit("]")
        }

        codeWriter.emit(")")
    }

    fun writeMiniParameters(
        data: ParameterData<MinimizedParameter>,
        codeWriter: CodeWriter,
        indent: Boolean = false,
    ) {
        if (!data.hasParameters) {
            codeWriter.emit("()")
            return
        }
        codeWriter.emit("(")
        data.positionalParameters.emitMiniParameters(codeWriter, forceNewLines = false)

        if (data.positionalParameters.isNotEmpty() && (data.hasAdditionalParameters || data.optionalAndDefault.isNotEmpty())) {
            codeWriter.emit(", ")
        }

        if (data.hasAdditionalParameters) {
            emitMiniRequiredAndNamedParameter(data, codeWriter, indent)
        }

        if (data.optionalAndDefault.isNotEmpty()) {
            codeWriter.emit("[")
            data.optionalAndDefault.emitMiniParameters(codeWriter, forceNewLines = false)
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
     */
    private fun emitRequiredAndNamedParameter(
        data: ParameterData<ParameterSpec>,
        codeWriter: CodeWriter,
        indent: Boolean = false,
    ) {
        codeWriter.emit("$CURLY_OPEN")

        if (indent) {
            codeWriter.emit(NEW_LINE)
            codeWriter.indent()
        }

        internalParameterWrite(data.requiredParameters, data.namedParameters.isNotEmpty(), codeWriter)
        internalParameterWrite(data.namedParameters, codeWriter = codeWriter)

        if (indent) {
            codeWriter.emit(NEW_LINE)
            codeWriter.unindent()
        }

        codeWriter.emit("$CURLY_CLOSE")
    }

    private fun emitMiniRequiredAndNamedParameter(
        data: ParameterData<MinimizedParameter>,
        codeWriter: CodeWriter,
        indent: Boolean = false,
    ) {
        codeWriter.emit("$CURLY_OPEN")

        if (indent) {
            codeWriter.emit(NEW_LINE)
            codeWriter.indent()
        }

        internalMiniParameterWrite(data.requiredParameters, data.namedParameters.isNotEmpty(), codeWriter)
        internalMiniParameterWrite(data.namedParameters, codeWriter = codeWriter)

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

    private fun internalMiniParameterWrite(
        parameters: List<MinimizedParameter>,
        trailingComma: Boolean = false,
        codeWriter: CodeWriter
    ) {
        if (parameters.isEmpty()) return

        parameters.emitMiniParameters(codeWriter, forceNewLines = false)
        if (trailingComma) {
            codeWriter.emit(COMMA_SEPARATOR)
        }
    }
}
