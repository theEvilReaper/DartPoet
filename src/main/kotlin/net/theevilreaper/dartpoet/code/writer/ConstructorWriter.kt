package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.DocumentationAppender
import net.theevilreaper.dartpoet.code.Writeable
import net.theevilreaper.dartpoet.code.emitParameters
import net.theevilreaper.dartpoet.function.DelegationWriterAdapter
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.NEW_LINE
import net.theevilreaper.dartpoet.util.ROUND_CLOSE
import net.theevilreaper.dartpoet.util.ROUND_OPEN
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE
import net.theevilreaper.dartpoet.util.StringHelper
import org.jetbrains.annotations.ApiStatus.Internal

/**
 * The [ConstructorWriter] includes the logic to write a [ConstructorSpec] reference into a [CodeWriter] instance.
 * A constructor in Dart can be sometimes a bit different compared to other definitions. To handle this the write process
 * checks the given data from the [ConstructorSpec] reference to decide the generation use more data.
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
@Internal
internal class ConstructorWriter : Writeable<ConstructorSpec>, DocumentationAppender {

    /**
     * Writes the given [ConstructorSpec] into the [CodeWriter] instance.
     * @param spec the [ConstructorSpec] to write
     * @param writer the [CodeWriter] instance to append the generated code into a [Appendable]
     */
    override fun write(spec: ConstructorSpec, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)

        val modifiers = getModifiersAsString(spec.isConst, spec.modifiers)
        writer.emitCode("%L", modifiers)

        writer.emit(spec.name)

        if (spec.isNamed) {
            writer.emitCode(".%L", spec.named)
        }

        if (!spec.hasParameters) {
            writer.emitCode("%L%L%L", ROUND_OPEN, ROUND_CLOSE, SEMICOLON)
            return
        }

        writer.emit(ROUND_OPEN)

        spec.parameters.emitParameters(writer)

        if (spec.hasNamedParameters) {
            if (spec.parameters.isNotEmpty()) {
                writer.emit(",$NEW_LINE")
            }
            writer.emit("$CURLY_OPEN")
            writer.emit(NEW_LINE)
            writer.indent()

            spec.requiredAndNamedParameters.emitParameters(writer, emitSpace = false, forceNewLines = true)

            writer.unindent()
            writer.emit(NEW_LINE)
            writer.emit("$CURLY_CLOSE")
        }
        writer.emit(ROUND_CLOSE)
        DelegationWriterAdapter.appendConstructorDelegation(spec.delegation, spec.initializer.build(), writer)
    }

    /**
     * Returns the involved modifiers from the [ConstructorSpec] as a string representation which is used for the generation.
     * @param isConst the flag to check if the constructor is a const constructor
     * @param modifiers the [Set] of [DartModifier] to join
     * @return the joined string or an empty string if the [Set] is empty
     */
    private fun getModifiersAsString(isConst: Boolean, modifiers: Set<DartModifier>): String {
        return when (isConst) {
            true -> StringHelper.joinModifiers(modifiers, separator = SPACE, postfix = SPACE)
            else -> if (modifiers.contains(DartModifier.CONST)) {
                "${DartModifier.CONST.identifier}·"
            } else {
                EMPTY_STRING
            }
        }
    }
}
