package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.parameter.minimized.MinimizedParameter

/**
 * The [MinimizedParameterWriter] is designed to write the structure of a [MinimizedParameter] to a [CodeWriter].
 * How the parameter is written by the language definition from Dart.
 * The main goal is to provide a well formatted output.
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
internal class MinimizedParameterWriter : Writeable<MinimizedParameter> {

    /**
     * Writes the given [MinimizedParameter] to the [CodeWriter].
     * @param spec the [MinimizedParameter] which is involved
     * @param writer the [CodeWriter] instance to write the code
     */
    override fun write(spec: MinimizedParameter, writer: CodeWriter) {
        if (spec.type == ParameterType.REQUIRED) {
            writer.emitCode("%L", DartModifier.REQUIRED.identifier)
            writer.emitSpace()
        }
        if (spec.self) {
            writer.emit("this.")
        }
        writer.emit(spec.name)
    }
}