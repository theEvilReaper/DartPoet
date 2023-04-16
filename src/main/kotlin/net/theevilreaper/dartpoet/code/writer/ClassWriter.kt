package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.NEW_LINE

class ClassWriter {

    fun write(classSpec: DartClassSpec, codeWriter: CodeWriter) {
        for (modifier in classSpec.modifiers) {
            codeWriter.emit("${modifier.identifier}·")
        }
        codeWriter.emit("${classSpec.name}·")

        codeWriter.emit("{\n")

        codeWriter.emit("}")

        if (classSpec.endsWithNewLine) {
            codeWriter.emit(NEW_LINE)
        }
    }
}