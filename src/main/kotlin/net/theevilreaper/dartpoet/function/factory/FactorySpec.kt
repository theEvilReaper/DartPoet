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
import net.theevilreaper.dartpoet.util.ParameterFilter
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * The [FactorySpec] represents the factory construct from the language Dart.
 * The language allows the definition of factory constructors which works like a normal constructor but with a
 * different syntax.
 */
class FactorySpec(
    builder: FactoryBuilder
): ConstructorBase {
    val typeName: TypeName = builder.typeName
    val isConst: Boolean = builder.const
    val documentation: CodeBlock = builder.documentation.build()
    val parameters: List<ParameterSpec> = builder.parameters.toImmutableList()
    val initializerBlock: CodeBlock = builder.initializerBlock.build()
    val withLambda: Boolean = builder.lambda
    val named: String? = builder.namedString
    val hasNamedData = named.orEmpty().trim().isNotEmpty()
    val parameterWithoutType: List<ParameterSpec> = builder.parameters.filter { it.type == null }.toMutableList()
    val hasParameterWithoutType get() = parameterWithoutType.isNotEmpty()
    val hasParameter: Boolean get() = parameters.isNotEmpty()
    internal val parametersWithDefaults =
        ParameterFilter.filterParameter(parameters) { !it.isRequired && it.hasInitializer }
    internal val requiredParameter =
        ParameterFilter.filterParameter(parameters) { it.isRequired && !it.isNamed && !it.hasInitializer }
    internal val namedParameter = ParameterFilter.filterParameter(parameters) { it.isNamed }
    internal val normalParameter =
        parameters.minus(parametersWithDefaults).minus(requiredParameter).minus(namedParameter).toImmutableList()
    internal val hasParameters = parameters.isNotEmpty()
    internal val hasAdditionalParameters = requiredParameter.isNotEmpty() || namedParameter.isNotEmpty()

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

        @JvmStatic
        fun constBuilder(typeName: TypeName) = FactoryBuilder(typeName, true)

        @JvmStatic
        fun constBuilder(className: ClassName) = FactoryBuilder(className, true)
    }
}