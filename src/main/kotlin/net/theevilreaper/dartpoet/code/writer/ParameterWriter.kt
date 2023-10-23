package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.parameter.ParameterSpec

/**
 * The ParameterWriter is used to write each parameter.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
internal class ParameterWriter : Writeable<ParameterSpec>, InitializerAppender {

    /**
     * The method contains the main logic to write a [ParameterSpec] to code.
     * It should be noted that the writer doesn't check whether the spec contains errors.
     * This would be done when the spec is being created
     */
    override fun write(spec: ParameterSpec, writer: CodeWriter) {
        spec.annotations.emitAnnotations(writer, endWithNewLine = false)

        if (spec.type != null) {
            writer.emitCode("%T", spec.type)
        } else {
            if (spec.isRequired) {
                writer.emit("${DartModifier.REQUIRED.identifier}·")
            }
            writer.emit("this.")
        }
        writer.emit(if (spec.isNullable) "?·" else if (spec.type != null) "·" else "")
        writer.emit(spec.name)
        writeInitBlock(spec.initializer ?: CodeBlock.EMPTY, writer)
    }
}
