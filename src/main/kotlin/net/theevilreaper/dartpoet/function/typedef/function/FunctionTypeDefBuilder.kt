package net.theevilreaper.dartpoet.function.typedef.function

import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.function.typedef.alias.AliasTypeDefBuilder
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import kotlin.reflect.KClass

class FunctionTypeDefBuilder(
    val type: TypeName,
) {

    /**
     * List of parameters associated with the type definition.
     */
    val parameters: MutableList<ParameterSpec> = mutableListOf()

    /**
     * The return type of the type definition.
     */
    var returnType: TypeName = Void::class.asTypeName()

    /**
     * Sets the return type of the type definition.
     *
     * @param typeName the return type as a [TypeName].
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun returns(typeName: TypeName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the type definition.
     *
     * @param typeName the return type as a [ClassName].
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun returns(typeName: ClassName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the type definition using a [KClass].
     *
     * @param typeName the return type as a [KClass].
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun returns(typeName: KClass<*>) = apply {
        this.returnType = typeName.asTypeName()
    }

    /**
     * Adds a parameter to the list of parameters.
     *
     * @param parameterSpec the parameter specification.
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun parameter(parameterSpec: ParameterSpec) = apply {
        this.parameters += parameterSpec
    }

    /**
     * Adds multiple parameters to the list of parameters.
     *
     * @param parameterSpecs the parameter specifications.
     * @return the current instance of [AliasTypeDefBuilder].
     */
    fun parameters(vararg parameterSpecs: ParameterSpec) = apply {
        this.parameters += parameterSpecs
    }

    /**
     * Creates a new [FunctionTypeDefSpec] object using the settings and data defined in the associated builder.
     * @return the created [FunctionTypeDefSpec] object.
     */
    fun build(): AbstractTypeDef<*> = FunctionTypeDefSpec(this)
}