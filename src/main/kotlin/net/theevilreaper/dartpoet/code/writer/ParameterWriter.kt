package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.util.EMPTY_STRING

/**
 * The ParameterWriter is used to write each parameter.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
internal class ParameterWriter : Writeable<ParameterSpec>, InitializerAppender<ParameterSpec> {

    /**
     * The method contains the main logic to write a [ParameterSpec] to code.
     * It should be noted that the writer doesn't check whether the spec contains errors.
     * This would be done when the spec is being created
     */
    override fun write(spec: ParameterSpec, writer: CodeWriter) {
        spec.annotations.emitAnnotations(writer, endWithNewLine = false)

        if (spec.type == ParameterType.REQUIRED) {
            writer.emit(DartModifier.REQUIRED.identifier)
            writer.emitSpace()
        }

        if (spec.typeName != null) {
            writer.emitCode("%T", spec.typeName)
        }
        val emitNullable = if (spec.isNullable && spec.typeName != null) "?·" else if (spec.typeName != null) "·" else EMPTY_STRING
        writer.emit(emitNullable)
        if (spec.hasNoTypeName) {
            writer.emit("this.")
        }
        writer.emit(spec.name)
        writeInitBlock(spec, writer)
    }

    override fun writeInitBlock(spec: ParameterSpec, writer: CodeWriter, isConstantContext: Boolean) {
        val initBlock = spec.initializer ?: CodeBlock.EMPTY
        if (initBlock.isEmpty()) return
        if (spec.isNamed && !spec.hasInitializer) return
        writer.emitSpace()
        writer.emit("=")
        writer.emitSpace()
        writer.emitCode(initBlock, isConstantContext)
    }
}
