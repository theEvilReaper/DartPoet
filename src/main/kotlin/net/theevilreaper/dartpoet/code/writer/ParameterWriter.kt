package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.parameter.DartParameterSpec

/**
 * The ParameterWriter is used to write each parameter.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
class ParameterWriter {

    /**
     * The method contains the main logic to write a [DartParameterSpec] to code.
     * It should be noted that the writer doesn't check whether the spec contains errors.
     * This would be done when the spec is being created
     */
    fun write(spec: DartParameterSpec, codeWriter: CodeWriter) {
        codeWriter.emit(spec.type)
        codeWriter.emit(if (spec.isNullable) "?·" else "·")
        codeWriter.emit(spec.name)

        writeInitializer(spec.initializer, codeWriter)
    }

    /**
     * Writes the given initializer [CodeBlock] from an [DartParameterSpec].
     * The methods do nothing when the block is null
     * @param codeBlock the given [CodeBlock] from the spec
     * @param codeWriter the [CodeWriter] instance to write the code
     */
    private fun writeInitializer(codeBlock: CodeBlock?, codeWriter: CodeWriter) {
        if (codeBlock != null) {
            codeWriter.emit("·=·")
            codeWriter.emitCode(if (codeBlock.hasStatements()) "%L" else "«%L»", codeBlock)
        }
    }
}