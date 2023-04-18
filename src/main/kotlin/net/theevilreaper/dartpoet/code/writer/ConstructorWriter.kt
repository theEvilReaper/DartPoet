package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec

class ConstructorWriter {

    private val factoryMethodBuilder = FactoryMethodBuilder()

    fun emit(spec: ConstructorSpec, codeWriter: CodeWriter) {
        if (spec.isFactory) {
            factoryMethodBuilder.write(spec, codeWriter)
        } else {
        }
    }
}