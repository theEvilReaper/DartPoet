package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.InitializerAppender
import net.theevilreaper.dartpoet.code.VariableAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE

/**
 * The [ConstantPropertyWriter] is responsible for writing the data of the [ConstantPropertySpec] to a [CodeWriter].
 * @since 1.0.0
 * @author theEvilReaper
 */
internal class ConstantPropertyWriter : Writeable<ConstantPropertySpec>, InitializerAppender<PropertyWriter>,
    VariableAppender {

    /**
     * Writes the data from a provided [ConstantPropertySpec] to the given [CodeWriter] instance.
     *
     * @param spec the ConstantPropertySpec which should be written
     * @param writer the instance from the [CodeWriter] to append the data
     **/
    override fun write(spec: ConstantPropertySpec, writer: CodeWriter) {
        val modifiersAsString = spec.modifiers.joinToString(separator = SPACE, postfix = SPACE) { it.identifier }

        writer.emit(modifiersAsString)

        if (spec.typeName != null) {
            writer.emitCode("%T·", spec.typeName)
        }

        writer.emitCode("%L", ensureVariableNameWithPrivateModifier(spec.isPrivate, spec.name))
        writeInitBlock(spec.initializer.build(), writer)
        writer.emit(SEMICOLON)
    }
}
