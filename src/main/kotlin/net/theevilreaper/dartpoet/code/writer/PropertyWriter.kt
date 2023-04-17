package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.property.DartPropertySpec
import net.theevilreaper.dartpoet.util.SPACE

class PropertyWriter {

    //TODO: Add delegation for the writer
    val annotationWriter = AnnotationWriter()

    //TODO: Write annotations
    fun write(property: DartPropertySpec, codeWriter: CodeWriter) {
        if (property.annotations.isNotEmpty()) {
            //TODO: Detect when to use inline
            property.annotations.forEach { annotationWriter.emit(it, codeWriter, inline = false) }
            codeWriter.emit("\n")
        }

        for (modifier in property.modifiers) {
            codeWriter.emit(modifier.identifier)
            codeWriter.emit(SPACE)
        }

        codeWriter.emit(property.type)

        if (property.nullable) {
            codeWriter.emit("? ")
        } else {
            codeWriter.emit(SPACE)
        }

        codeWriter.emit(if (property.isPrivate) DartModifier.PRIVATE.identifier else "")
        codeWriter.emit(property.name)

        if (property.initBlock.isNotEmpty()) {
            codeWriter.emit("$SPACE=$SPACE")
            codeWriter.emitCode(property.initBlock.build(), isConstantContext = true)
        }
        codeWriter.emitCode(";")
    }
}