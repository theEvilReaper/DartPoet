package net.theevilreaper.dartpoet.operator

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName
import org.jetbrains.annotations.Contract

class DartOperatorSpec(
    val builder: DartOperatorBuilder
) {
    val operator: DartOperator = builder.operator
    val parameters: List<ParameterSpec> = builder.parameters
    val returnType: TypeName? = builder.returnType

    /**
     * Creates a new instance of [DartOperatorBuilder] with the same properties as this instance.
     * @return the created [DartOperatorBuilder] instance
     */
    @Contract(pure = true)
    fun toBuilder(): DartOperatorBuilder {
        val builder = DartOperatorBuilder(this.operator)
        builder.parameters.addAll(this.parameters)
        builder.returnType = this.returnType
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
