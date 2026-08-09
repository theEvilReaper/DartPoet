package net.theevilreaper.dartpoet.operator

class DartOperatorBuilder {

    var operator: DartOperator? = null
    var parameters: List<String> = emptyList()
    var returnType: String? = null

    /**
     * Sets the operator type.
     *
     * @param operator the operator type
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun operator(operator: DartOperator) = apply { this.operator = operator }

    /**
     * Sets the parameters for the operator.
     *
     * @param parameters the parameters for the operator
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun parameters(parameters: List<String>) = apply { this.parameters = parameters }

    /**
     * Sets the return type of the operator.
     *
     * @param returnType the return type of the operator
     * @return the given instance of a [DartOperatorBuilder]
     */
    fun returnType(returnType: String) = apply { this.returnType = returnType }

    /**
     * Builds the [DartOperatorSpec] instance.
     *
     * @return the built [DartOperatorSpec] instance
     */
    fun build(): DartOperatorSpec = DartOperatorSpec(this)
}
