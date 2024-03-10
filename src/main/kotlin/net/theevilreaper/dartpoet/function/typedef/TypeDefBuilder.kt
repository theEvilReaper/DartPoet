package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import kotlin.reflect.KClass

/**
 * The builder is used to create a type definition with a specific name and optional type cast.
 * After the construction the builder maps the data into a [TypeDefSpec] object.
 *
 * @param typeDefName the name of the type definition.
 * @param typeCasts optional array of type-cast for the type definition.
 */
class TypeDefBuilder internal constructor(
    val typeDefName: String,
    vararg val typeCasts: TypeName? = emptyArray()
) {
    /**
     * The name of the type definition.
     */
    var name: String? = null

    /**
     * The return type of the type definition.
     */
    var returnType: TypeName? = null

    /**
     * List of parameters associated with the type definition.
     */
    val parameters: MutableList<ParameterSpec> = mutableListOf()

    /**
     * Sets the name of the type definition.
     *
     * @param name the name of the type definition.
     * @return the current instance of [TypeDefBuilder].
     */
    fun name(name: String) = apply {
        this.name = name
    }

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
     * Sets the return type of the type definition.
     *
     * @param typeName the return type as a [TypeName].
     * @return the current instance of [TypeDefBuilder].
     */
    fun returns(typeName: TypeName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the type definition.
     *
     * @param typeName the return type as a [ClassName].
     * @return the current instance of [TypeDefBuilder].
     */
    fun returns(typeName: ClassName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the type definition using a [KClass].
     *
     * @param typeName the return type as a [KClass].
     * @return the current instance of [TypeDefBuilder].
     */
    fun returns(typeName: KClass<*>) = apply {
        this.returnType = typeName.asTypeName()
    }

    /**
     * Builds and returns an instance of [TypeDefSpec] based on the configuration.
     *
     * @return an instance of [TypeDefSpec].
     */
    fun build(): TypeDefSpec {
        return TypeDefSpec(this)
    }
}
