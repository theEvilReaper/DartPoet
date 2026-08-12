package net.theevilreaper.dartpoet.operator

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.util.toImmutableSet
import org.jetbrains.annotations.Contract

/**
 * The spec class contains all relevant information about an operator overload in dart.
 * An [net.theevilreaper.dartpoet.code.writer.operator.OperatorWriter] instance reads the data
 * from it to write the operator declaration into the class structure from dart.
 * @param builder the builder instance to retrieve the data from
 * @author theEvilReaper
 * @since 2.1.0
 */
class DartOperatorSpec internal constructor(
    builder: DartOperatorBuilder
) {
    internal val operator: DartOperator = builder.operator
    internal val parameters: List<ParameterSpec> = builder.parameters.toImmutableList()
    internal val returnType: TypeName = checkNotNull(builder.returnType) { "The return type of an operator can't be null" }
    internal val body: CodeBlock = builder.body.build()
    internal val docs: List<CodeBlock> = builder.docs.toImmutableList()
    internal val annotations: Set<AnnotationSpec> = builder.annotationData.annotations.toImmutableSet()
    internal val type: FunctionType = builder.type

    internal val hasParameters: Boolean = parameters.isNotEmpty()

    init {
        val requiredParameterCount = when (val op = operator) {
            is UnaryOperator -> 0
            is BinaryOperator -> 1
            is IndexOperator -> when (op) {
                IndexOperator.INDEX -> 1
                IndexOperator.INDEX_ASSIGN -> 2
            }
        }

        check(parameters.size == requiredParameterCount) {
            "Operator '${operator.symbol}' requires exactly $requiredParameterCount parameter(s), but got ${parameters.size}"
        }

        parameters.forEach {
            check(it.type == ParameterType.POSITIONAL) {
                "Operator parameters must be simple positional parameters, but got ${it.type} for '${it.name}'"
            }
            check(!it.hasInitializer) {
                "Operator parameters can't have a default value ('${it.name}')"
            }
        }

        check(body.isNotEmpty()) { "An operator must have a body" }
    }

    /**
     * Calls the [net.theevilreaper.dartpoet.code.writer.operator.OperatorWriter] to write the content
     * from this spec into a [CodeWriter].
     * @param codeWriter the writer instance
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.operatorWriter.write(this, codeWriter)
    }

    /**
     * Returns a [String] representation from the operator spec.
     * @return the generated representation as string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Creates a new instance of [DartOperatorBuilder] with the same properties as this instance.
     * @return the created [DartOperatorBuilder] instance
     */
    @Contract(pure = true)
    fun toBuilder(): DartOperatorBuilder {
        val builder = DartOperatorBuilder(this.operator)
        builder.parameters.addAll(this.parameters)
        builder.returnType = this.returnType
        builder.body.add(this.body)
        builder.docs.addAll(this.docs)
        builder.annotations(*this.annotations.toTypedArray())
        builder.type(this.type)
        return builder
    }

    /**
     * The companion object contains some helper methods to create a new instance of a [DartOperatorBuilder].
     * @since 2.1.0
     * @author theEvilReaper
     */
    companion object {

        /**
         * Creates a new instance of [DartOperatorBuilder] with the specified operator.
         * @param operator the operator to build the builder for
         * @return the created [DartOperatorBuilder] instance
         */
        @JvmStatic
        fun builder(operator: DartOperator): DartOperatorBuilder = DartOperatorBuilder(operator)
    }
}
