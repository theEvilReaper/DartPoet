package net.theevilreaper.dartpoet.function.factory

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.FactoryMethodBuilder
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.util.toImmutableSet

class FactoryFunctionSpec(
    val builder: FactoryFunctionBuilder
) : FunctionType {

    val name = builder.name
    val classType = builder.classType
    val parameters = builder.parameters.toImmutableSet()
    val isLambda = builder.lambda
    val body = builder.body

    internal fun write(
        codeWriter: CodeWriter
    ) {
        FactoryMethodBuilder().write(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this
        )
    }

    companion object {

        @JvmStatic
        fun builder() = FactoryFunctionBuilder()
    }




}