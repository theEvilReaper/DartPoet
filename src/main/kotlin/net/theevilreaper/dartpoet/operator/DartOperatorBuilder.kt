package net.theevilreaper.dartpoet.operator

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.function.FunctionType
import net.theevilreaper.dartpoet.meta.AnnotationData
import net.theevilreaper.dartpoet.meta.AnnotationMethods
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.util.NO_PARAMETER_TYPE

class DartOperatorBuilder internal constructor(
    val operator: DartOperator
) : AnnotationMethods<DartOperatorBuilder> {

    internal val parameters: MutableList<ParameterSpec> = mutableListOf()
    var returnType: TypeName? = null
    internal val annotationData: AnnotationData = AnnotationData()
    internal val body: CodeBlock.Builder = CodeBlock.builder()
    internal val docs: MutableList<CodeBlock> = mutableListOf()
    internal var type: FunctionType = FunctionType.STANDARD

    /**
     * Adds a parameter to the operator.
     *
     * @param parameter the parameter to add
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun parameter(parameter: ParameterSpec) = apply {
        check(!parameter.hasNoTypeName) { NO_PARAMETER_TYPE }
        this.parameters.add(parameter)
    }

    /**
     * Sets the parameters for the operator.
     *
     * @param parameters the parameters for the operator
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun parameters(parameters: List<ParameterSpec>) = apply {
        parameters.forEach {
            check(!it.hasNoTypeName) { NO_PARAMETER_TYPE }
        }
        this.parameters.addAll(parameters)
    }

    /**
     * Sets the return type of the operator.
     *
     * @param returnType the return type of the operator
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun returnType(returnType: TypeName) = apply { this.returnType = returnType }

    /**
     * Adds a documentation comment line to the operator.
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun doc(format: String, vararg args: Any) = apply {
        this.docs.add(CodeBlock.of(format.replace(' ', '·'), *args))
    }

    /**
     * Sets whether the operator body is written as a block (`{ }`) or a shorthand (`=>`).
     * @param type the [FunctionType] to use
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun type(type: FunctionType) = apply { this.type = type }

    /**
     * Adds formatted code to the operator body.
     * @param format the string which contains the content and the format
     * @param args the arguments for the format string
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun addCode(format: String, vararg args: Any?) = apply { this.body.add(format, *args) }

    /**
     * Adds a [CodeBlock] to the operator body.
     * @param codeBlock the code block to add
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun addCode(codeBlock: CodeBlock) = apply { this.body.add(codeBlock) }

    /**
     * Adds named formatted code to the operator body.
     * @param format the string which contains the content and the format
     * @param args the named arguments for the format string
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun addNamedCode(format: String, args: Map<String, *>) = apply { this.body.addNamed(format, args) }

    override fun annotation(annotation: () -> AnnotationSpec) = apply { this.annotationData.annotation(annotation) }

    override fun annotation(annotation: AnnotationSpec) = apply { this.annotationData.annotation(annotation) }

    override fun annotations(vararg annotations: AnnotationSpec) = apply { this.annotationData.annotations(*annotations) }

    /**
     * Builds the [DartOperatorSpec] instance.
     *
     * @return the built [DartOperatorSpec] instance
     */
    fun build(): DartOperatorSpec = DartOperatorSpec(this)
}
