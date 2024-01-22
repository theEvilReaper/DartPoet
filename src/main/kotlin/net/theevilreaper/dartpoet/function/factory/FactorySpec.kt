package net.theevilreaper.dartpoet.function.factory

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.FactoryWriter
import net.theevilreaper.dartpoet.function.ConstructorBase
import net.theevilreaper.dartpoet.function.constructor.ConstructorSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.toImmutableSet

class FactorySpec(
    builder: FactoryBuilder
): ConstructorBase {
    val typeName: TypeName = builder.typeName
    val documentation: CodeBlock = builder.documentation.build()
    val parameters: Set<ParameterSpec> = builder.parameters.toImmutableSet()
    val initializerBlock: CodeBlock = builder.initializerBlock.build()
    val withLambda: Boolean = builder.lambda
    val named: String? = builder.namedString
    val hasNamedData = named.orEmpty().trim().isNotEmpty()
    val hasParameter: Boolean get() = parameters.isNotEmpty()

    init {
        check(initializerBlock.isNotEmpty()) { "The initializer block must not be empty" }
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        FactoryWriter().write(this, codeWriter)
    }

    override fun toString() = buildCodeString { write(this) }

    companion object {

        @JvmStatic
        fun builder(typeName: TypeName) = FactoryBuilder(typeName)

        @JvmStatic
        fun builder(className: ClassName) = FactoryBuilder(className)
    }
}