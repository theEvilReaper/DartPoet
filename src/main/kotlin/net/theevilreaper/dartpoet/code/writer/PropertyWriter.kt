package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.*
import net.theevilreaper.dartpoet.code.emitAnnotations
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.SEMICOLON
import net.theevilreaper.dartpoet.util.SPACE
import net.theevilreaper.dartpoet.util.StringHelper

/**
 * The [PropertyWriter] contains the main logic to write a valid structure of a property for the programming language Dart.
 * It contains only the logic of the write process. Each validation is done by the [PropertySpec] itself on the creation of it.
 * @author theEvilReaper
 * @since 1.0.0
 */
internal class PropertyWriter : Writeable<PropertySpec>, DocumentationAppender,
    InitializerAppender<PropertySpec> {

    /**
     * Writes the given [PropertySpec] into the [CodeWriter].
     * @param spec the [PropertySpec] which is involved
     * @param writer the [CodeWriter] instance to write the code
     */
    override fun write(spec: PropertySpec, writer: CodeWriter) {
        emitDocumentation(spec.docs, writer)
        spec.annotations.emitAnnotations(writer) {
            it.write(writer, inline = false)
        }

        val modifierString = StringHelper.concatData(
            spec.modifiers,
            separator = SPACE,
            postfix = if (spec.hasModifiers) SPACE else EMPTY_STRING
        ) { it.identifier }
        writer.emit(modifierString)

        if (spec.type != null) {
            writer.emitCode("%T", spec.type)
            writer.emitSpace()
        }

        writer.emit(StringHelper.ensureVariableNameWithPrivateModifier(spec.name, spec.isPrivate))
        writeInitBlock(spec.initBlock.build(), writer)
        writer.emit(SEMICOLON)
    }
}
