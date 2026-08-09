package net.theevilreaper.dartpoet.operator

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName

class DartOperatorSpec(
    val builder: DartOperatorBuilder
) {

    val operator: DartOperator
    val parameters: List<ParameterSpec>
    val returnType: TypeName

    companion object {

        fun builder(): DartOperatorBuilder = DartOperatorBuilder()
    }

}