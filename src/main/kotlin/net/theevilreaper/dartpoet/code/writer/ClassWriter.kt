package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import net.theevilreaper.dartpoet.function.factory.FactoryFunctionSpec
import net.theevilreaper.dartpoet.util.NEW_LINE

class ClassWriter {

    private val annotationWriter = AnnotationWriter()
    private val functionWriter = FunctionWriter()
    private val factoryMethodBuilder = FactoryMethodBuilder()

    fun write(classSpec: DartClassSpec, codeWriter: CodeWriter) {
        if (classSpec.annotations.isNotEmpty()) {
            classSpec.annotations.forEach { annotationWriter.emit(it, codeWriter, false) }
        }
        codeWriter.emit(NEW_LINE)
        for (modifier in classSpec.classModifiers) {
            codeWriter.emit("${modifier.identifier}·")
        }
        codeWriter.emit("${classSpec.name}·")

        if (classSpec.mixinSubClass.orEmpty().trim().isNotEmpty()) {
            codeWriter.emit("${DartModifier.WITH.identifier}·${classSpec.mixinSubClass}")
        }

        codeWriter.emit("·{\n")

        codeWriter.indent()

        if (classSpec.functions.isNotEmpty()) {
            classSpec.functions.forEach {
                if (it is FactoryFunctionSpec) {
                    factoryMethodBuilder.write(it, codeWriter)
                    return@forEach
                } else {
                    functionWriter.emit(it as DartFunctionSpec, codeWriter)
                }
            }
        }

        codeWriter.unindent()

        codeWriter.emit("}")

        if (classSpec.endsWithNewLine) {
            codeWriter.emit(NEW_LINE)
        }
    }
}