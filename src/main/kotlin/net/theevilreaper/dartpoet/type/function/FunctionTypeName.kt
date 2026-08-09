package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.parameter.ParameterChecker
import net.theevilreaper.dartpoet.parameter.ParameterContext
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NULLABLE_CHAR
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.parameter.ParameterData
import net.theevilreaper.dartpoet.function.typedef.function.FunctionTypeDefSpec

/**
 * Represents an anonymous, inline Dart function type such as `void Function(int count)?`.
 *
 * Unlike [FunctionTypeDefSpec], this type has no
 * name of its own and can be used anywhere a [TypeName] is accepted (property types, parameter types,
 * return types, generic type arguments).
 *
 * @param returnType the return type of the function type
 * @param parameters the parameters of the function type
 * @param isNullable whether the function type itself can be null (default is false)
 * @since 2.1.0
 * @author theEvilReaper
 */
class FunctionTypeName internal constructor(
    val returnType: TypeName,
    parameters: List<ParameterSpec>,
    isNullable: Boolean = false
) : TypeName(isNullable), ParameterContext<ParameterSpec> by ParameterContext(parameters) {

    init {
        ParameterChecker.checkOptionalParameters(parametersWithDefaults)
    }

    /**
     * Emits `returnType Function(params)` (optionally suffixed with `?`) to the given [CodeWriter].
     */
    override fun emit(out: CodeWriter): CodeWriter {
        out.emitCode("%T", returnType)
        out.emitSpace()
        out.emitCode("%T", Function::class.asTypeName())

        val parameterData = ParameterData.of(this)
        if (parameterData.hasParameters) {
            ParameterHelper.writeParameters(
                parameterData,
                out,
                indent = parameterData.requiredParameters.size > 1,
                writeInitializers = false,
            )
        } else {
            out.emitEmptyRoundBrackets()
        }

        if (isNullable) {
            out.emit(NULLABLE_CHAR)
        }
        return out
    }

    /**
     * Creates a copy of this [FunctionTypeName] with the given nullable flag.
     * @param nullable the nullable flag to set
     * @return a new [FunctionTypeName] instance with the provided nullable flag
     */
    override fun copy(nullable: Boolean): FunctionTypeName = FunctionTypeName(returnType, parameters, nullable)

    /**
     * Function types are never used as a generic self-reference target (see [ExtensionSpec][net.theevilreaper.dartpoet.extension.ExtensionSpec]),
     * so there is no meaningful raw data to expose.
     */
    override fun getRawData(): String = EMPTY_STRING

    companion object {

        /**
         * Creates a new [FunctionTypeNameBuilder] instance.
         * @return the created builder
         */
        @JvmStatic
        fun builder() = FunctionTypeNameBuilder()
    }
}
