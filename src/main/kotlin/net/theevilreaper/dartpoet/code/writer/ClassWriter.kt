package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.emitConstructors
import net.theevilreaper.dartpoet.code.emitFunctions
import net.theevilreaper.dartpoet.property.DartPropertySpec
import net.theevilreaper.dartpoet.util.NEW_LINE

class ClassWriter {

    private val abstractClassWriter = AbstractClassWriter()
    private val propertyWriter = PropertyWriter()
    private val annotationWriter = AnnotationWriter()
    private val functionWriter = FunctionWriter()
    private val constructorWriter = ConstructorWriter()
    private val libraryWriter = LibraryWriter()

    fun write(classSpec: DartClassSpec, codeWriter: CodeWriter) {
        if (classSpec.annotations.isNotEmpty()) {
            classSpec.annotations.forEach { annotationWriter.emit(it, codeWriter, false) }
            codeWriter.emit(NEW_LINE)
        }

        if (classSpec.isAbstract) {
            abstractClassWriter.write(classSpec, codeWriter)
            return
        }

        for (modifier in classSpec.classModifiers) {
            codeWriter.emit("${modifier.identifier}·")
        }
        codeWriter.emit("${classSpec.name}")

        if (classSpec.mixinSubClass.orEmpty().trim().isNotEmpty()) {
            codeWriter.emit("·${DartModifier.WITH.identifier}·${classSpec.mixinSubClass}")
        }

        codeWriter.emit("·{\n")

        codeWriter.indent()
        codeWriter.emit(NEW_LINE)

        classSpec.properties.emit(codeWriter) {
            it.write(codeWriter)
        }

        classSpec.constructors.emitConstructors(codeWriter) {
            it.write(codeWriter)
        }

        classSpec.functions.emitFunctions(codeWriter) {
            functionWriter.emit(it, codeWriter)
        }

        codeWriter.unindent()

        codeWriter.emit("}")

        if (classSpec.endsWithNewLine) {
            codeWriter.emit(NEW_LINE)
        }
    }

    private fun Set<DartPropertySpec>.emit(
        codeWriter: CodeWriter,
        forceNewLines: Boolean = false,
        emitBlock: (DartPropertySpec) -> Unit = { it.write(codeWriter) }
    ) = with(codeWriter) {
        if (isNotEmpty()) {
            val emitNewLines = size > 1 || forceNewLines

            forEachIndexed { index, property ->
                if (index > 0) {
                    emit(if (emitNewLines) NEW_LINE else "")
                }
                emitBlock(property)
            }
            emit(NEW_LINE)
        }
    }
}