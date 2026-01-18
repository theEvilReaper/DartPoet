package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import java.util.function.Function

class FunctionTypeDefBuilder(
    typeDefName: String,
    typeName: TypeName = Function::class.asTypeName(),
    vararg typeCasts: TypeName? = emptyArray(),
) : TypeDefBuilder<FunctionTypeDefBuilder>(typeDefName, typeName, *typeCasts) {

    /**
     * List of parameters associated with the type definition.
     */
    val parameters: MutableList<ParameterSpec> = mutableListOf()

    /**
     * Adds a parameter to the list of parameters.
     *
     * @param parameterSpec the parameter specification.
     * @return the current instance of [TypeDefBuilder].
     */
    fun parameter(parameterSpec: ParameterSpec) = apply {
        this.parameters += parameterSpec
    }

    /**
     * Adds multiple parameters to the list of parameters.
     *
     * @param parameterSpecs the parameter specifications.
     * @return the current instance of [TypeDefBuilder].
     */
    fun parameters(vararg parameterSpecs: ParameterSpec) = apply {
        this.parameters += parameterSpecs
    }

    /**
     * Creates a new [TypeDefSpec] object using the settings and data defined in the associated builder.
     * @return the created [TypeDefSpec] object.
     */
    override fun build(): TypeDefSpec = FunctionTypeDefSpec(this)
}