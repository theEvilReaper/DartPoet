package net.theevilreaper.dartpoet.operator

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName

class DartOperatorBuilder(
    val operator: DartOperator
) {

    var parameters: MutableList<ParameterSpec> = mutableListOf()
    var returnType: TypeName? = null

    /**
     * Adds a parameter to the operator.
     *
     * @param parameter the parameter to add
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun parameter(parameter: ParameterSpec) = apply { this.parameters.add(parameter) }

    /**
     * Sets the parameters for the operator.
     *
     * @param parameters the parameters for the operator
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun parameters(parameters: List<ParameterSpec>) = apply { this.parameters.addAll(parameters) }

    /**
     * Sets the return type of the operator.
     *
     * @param returnType the return type of the operator
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun returnType(returnType: TypeName) = apply { this.returnType = returnType }

    /**
     * Builds the [DartOperatorSpec] instance.
     *
     * @return the built [DartOperatorSpec] instance
     */
    fun build(): DartOperatorSpec = DartOperatorSpec(this)
}
