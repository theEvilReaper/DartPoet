package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitAnnotations
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
        spec.annotations.emitAnnotations(codeWriter, endWithNewLine = false) {
            it.write(codeWriter)
        }

        if (spec.type.orEmpty().trim().isNotEmpty()) {
            codeWriter.emit(spec.type!!)
        } else {
            if (spec.isRequired) {
                codeWriter.emit("required·")
            }
            codeWriter.emit("this.")
        }
        codeWriter.emit(if (spec.isNullable) "?·" else if (spec.type.orEmpty().trim().isNotEmpty()) "·" else "")
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
