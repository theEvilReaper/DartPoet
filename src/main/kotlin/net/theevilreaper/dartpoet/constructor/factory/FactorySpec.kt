package net.theevilreaper.dartpoet.constructor.factory

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.FactoryWriter
import net.theevilreaper.dartpoet.constructor.ConstructorBase
import net.theevilreaper.dartpoet.constructor.ConstructorDelegation
import net.theevilreaper.dartpoet.parameter.ParameterContext
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.ParameterBase
import net.theevilreaper.dartpoet.util.ParameterHelper
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet

/**
 * The [FactorySpec] represents the factory construct from the language Dart.
 * The language allows the definition of factory constructors which work like a normal constructor but with a
 * different syntax.
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
class FactorySpec(
    builder: FactoryBuilder,
) : ConstructorBase, ParameterContext<ParameterSpec> by FactoryParameterContext(builder.parameters.toImmutableList()) {
    val typeName: TypeName = builder.typeName
    val isConst: Boolean = builder.const
    val annotations: Set<AnnotationSpec> = builder.annotations.toImmutableSet()
    val documentation: CodeBlock = builder.documentation.build()
    val initializerBlock: CodeBlock = builder.initializerBlock.build()
    val named: String? = builder.namedString
    val hasNamedData = named.orEmpty().trim().isNotEmpty()
    val constructorDelegation: ConstructorDelegation = builder.delegation

    /**
     * Performs some checks on the spec object.
     */
    init {
        check(initializerBlock.isNotEmpty()) { "The initializer block must not be empty" }
    }

    /**
     * Writes the factory constructor to the given [CodeWriter].
     * @param codeWriter the writer to write the factory constructor
     * @see FactoryWriter
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.factoryWriter.write(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec object.
     * @return the spec object as string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object.
     * @return the builder instance
     */
    fun toBuilder(): FactoryBuilder {
        val builder = FactoryBuilder(typeName, isConst)
        builder.documentation.add(documentation)
        builder.annotations.addAll(annotations)
        builder.parameters.addAll(parameters)
        builder.initializerBlock.add(initializerBlock)
        builder.delegation = constructorDelegation
        builder.namedString = named
        return builder
    }

    /**
     * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object.
     * @author theEvilReaper
     * @since 1.0.0
     * @version 1.0.0
     */
    companion object {

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object.
         * @param typeName the [TypeName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun builder(typeName: TypeName) = FactoryBuilder(typeName)

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object.
         * @param className the [ClassName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun builder(className: ClassName) = FactoryBuilder(className)

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object which is const by default.
         * @param typeName the [TypeName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun constBuilder(typeName: TypeName) = FactoryBuilder(typeName, true)

        /**
         * Creates a new [FactoryBuilder] instance to build a new [FactorySpec] object which is const by default.
         * @param className the [ClassName] of the factory
         * @return the builder instance
         */
        @JvmStatic
        fun constBuilder(className: ClassName) = FactoryBuilder(className, true)

        /**
         * Creates a new instance of [ParameterContext] from the given list of parameters.
         * @param parameters the list of parameters to analyze
         */
        operator fun <T : ParameterBase> invoke(parameters: List<T>): ParameterContext<T> =
            FactoryParameterContext(parameters)
    }

    /**
     * Internal implementation of the [ParameterContext] interface for factory constructors.
     * @param T the type of the parameter, must extend [ParameterBase]
     * @param parameters the list of parameters to analyze
     * @author theEvilReaper
     * @since 1.0.0
     * @version 1.0.0
     */
    private class FactoryParameterContext<T : ParameterBase>(parameters: List<T>) : ParameterContext<T> {
        override val parameters = parameters.toImmutableList()

        override val optionalNamed = this.parameters.filter {
            it.type == ParameterType.NAMED
        }.toImmutableList()

        override val requiredParameters = this.parameters.filter {
            it.type == ParameterType.REQUIRED
        }.toImmutableList()

        override val parametersWithDefaults = this.parameters.filter {
            it.type == ParameterType.OPTIONAL
        }.toImmutableList()

        override val normalParameters: List<T> = ParameterHelper.excludeParameters(
            this.parameters,
            optionalNamed,
            requiredParameters,
            parametersWithDefaults
        )

        override val hasParameters = this.parameters.isNotEmpty()

        override val hasAdditionalParameters = requiredParameters.isNotEmpty() || optionalNamed.isNotEmpty()
    }
}