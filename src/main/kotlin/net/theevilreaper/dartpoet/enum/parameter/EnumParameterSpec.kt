package net.theevilreaper.dartpoet.enum.parameter

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.ROUND_CLOSE
import net.theevilreaper.dartpoet.util.ROUND_OPEN

/**
 * The [EnumParameterSpec] represents the value entries which a [EnumEntrySpec] can have.
 * Each parameter can have the required flag or not. This flag means that the parameter is required to be set.
 * Is this flag is set to true the [variableRef] must be set due to the [Dart language specification]("https://dart.dev/language/enums").
 *
 * <bold>Be aware that the given [dataBlock] must have only one argument.</bold>
 * <bold>Otherwise the creation of the [EnumParameterSpec] will fail.</bold>
 *
 * @param dataBlock the data block for the parameter
 * @param required the flag to determine if the parameter is required
 * @param variableRef the variable reference for the parameter
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
data class EnumParameterSpec(
    val dataBlock: CodeBlock,
    val required: Boolean,
    val variableRef: String? = null
) {
    init {
        check(!dataBlock.isEmpty()) { "The data block can't be empty" }
        require(dataBlock.args.size <= 1) { "The data block can't have more than one argument" }
        require(!(required && (variableRef == null || variableRef.trim().isEmpty()))) {
            "The variable reference can't be null when the parameter is required"
        }
    }

    /**
     * Writes the given [EnumParameterSpec] to the [CodeWriter] instance.
     * @param codeWriter the [CodeWriter] to apply the content from the spec
     * @see CodeWriter
     */
    internal fun write(codeWriter: CodeWriter) {
        when (required) {
            false -> codeWriter.emitCode(dataBlock, isConstantContext = false, ensureTrailingNewline = false)
            true -> {
                codeWriter.emitCode("%L:", variableRef)
                codeWriter.emitSpace()
                codeWriter.emitCode(dataBlock, isConstantContext = false, ensureTrailingNewline = false)
            }
        }
    }

    /**
     * Returns the string representation of the [EnumParameterSpec].
     * @return the string representation
     */
    override fun toString(): String = buildCodeString { write(this) }

    /**
     * Companion object to create new instances of the [EnumParameterSpec].
     */
    companion object {

        /**
         * Creates a new [EnumParameterSpec] instance with the given [format] and [args].
         * @param format the format string
         * @param args the arguments for the format string
         * @return the created instance
         */
        @JvmStatic
        fun from(format: String, vararg args: Any): EnumParameterSpec = EnumParameterSpec(
            CodeBlock.of(format, *args),
            false
        )

        /**
         * Creates a new [EnumParameterSpec] instance with the given [format] and [args].
         * @param format the format string
         * @param args the arguments for the format string
         * @param variableRef the variable reference for the parameter
         * @return the created instance
         */
        @JvmStatic
        fun required(format: String, vararg args: Any, variableRef: String) = EnumParameterSpec(
            CodeBlock.of(format, *args),
            true,
            variableRef
        )
    }
}

/**
 * Emits the [EnumParameterSpec] to the [CodeWriter] instance.
 * @param codeWriter the [CodeWriter] to apply the content from the spec
 * @param emitBlock the block to emit the [EnumParameterSpec]
 */
fun List<EnumParameterSpec>.emitEnumParameterSpecs(
    codeWriter: CodeWriter,
    emitBlock: (EnumParameterSpec) -> Unit = { it.write(codeWriter) }
) = with(codeWriter) {
    if (isEmpty()) return@with
    codeWriter.emit(ROUND_OPEN)
    forEachIndexed { index, parameter ->
        if (index > 0) {
            emit(COMMA_SEPARATOR)
        }
        emitBlock(parameter)
    }
    codeWriter.emit(ROUND_CLOSE)
}