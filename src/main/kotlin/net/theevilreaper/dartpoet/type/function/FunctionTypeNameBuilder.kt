package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import kotlin.reflect.KClass

/**
 * Builder for [FunctionTypeName]. Entered via [FunctionTypeName.builder].
 * @since 2.1.0
 * @author theEvilReaper
 */
class FunctionTypeNameBuilder internal constructor() {

    private var returnType: TypeName = Void::class.asTypeName()
    private val parameters: MutableList<ParameterSpec> = mutableListOf()
    private var nullable: Boolean = false

    /**
     * Sets the return type of the function type.
     * @param typeName the return type as a [TypeName]
     * @return the current instance of [FunctionTypeNameBuilder]
     */
    fun returns(typeName: TypeName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the function type.
     * @param typeName the return type as a [ClassName]
     * @return the current instance of [FunctionTypeNameBuilder]
     */
    fun returns(typeName: ClassName) = apply {
        this.returnType = typeName
    }

    /**
     * Sets the return type of the function type.
     * @param typeName the return type as a [KClass]
     * @return the current instance of [FunctionTypeNameBuilder]
     */
    fun returns(typeName: KClass<*>) = apply {
        this.returnType = typeName.asTypeName()
    }

    /**
     * Adds a parameter to the list of parameters.
     * @param parameterSpec the parameter specification
     * @return the current instance of [FunctionTypeNameBuilder]
     */
    fun parameter(parameterSpec: ParameterSpec) = apply {
        this.parameters += parameterSpec
    }

    /**
     * Adds multiple parameters to the list of parameters.
     * @param parameterSpecs the parameter specifications
     * @return the current instance of [FunctionTypeNameBuilder]
     */
    fun parameters(vararg parameterSpecs: ParameterSpec) = apply {
        this.parameters += parameterSpecs
    }

    /**
     * Sets whether the function type itself is nullable.
     * @param nullable whether the function type can be null (default is true)
     * @return the current instance of [FunctionTypeNameBuilder]
     */
    fun nullable(nullable: Boolean = true) = apply {
        this.nullable = nullable
    }

    /**
     * Creates a new [FunctionTypeName] using the settings defined in this builder.
     * @return the created [FunctionTypeName] instance
     */
    fun build(): FunctionTypeName = FunctionTypeName(returnType, parameters, nullable)
}
