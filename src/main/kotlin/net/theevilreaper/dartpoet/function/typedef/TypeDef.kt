package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.function.typedef.alias.AliasTypeDefBuilder
import net.theevilreaper.dartpoet.function.typedef.function.FunctionTypeDefBuilder
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import net.theevilreaper.dartpoet.type.TypeName

/**
 * The [TypeDef] is a factory class to create different instance of a [AliasTypeDefBuilder] to create type def declarations
 *
 * @since 1.0.0
 * @author theEvilReaper
 **/
object TypeDef {

    /**
     * Creates a new [AliasTypeDefBuilder] for defining an alias typedef with the given name.
     *
     * @param name the name of the typedef
     * @return a new [AliasTypeDefBuilder] instance
     */
    fun alias(name: String): AliasTypeDefBuilder = AliasTypeDefBuilder(ClassName(name))

    /**
     * Creates a new [AliasTypeDefBuilder] for defining an alias typedef with type parameters.
     *
     * @param name the name of the typedef
     * @param typeCasts the generic type parameters of the typedef
     * @return a new [AliasTypeDefBuilder] instance
     */
    fun alias(
        typeName: TypeName
    ): AliasTypeDefBuilder =
        AliasTypeDefBuilder(typeName)

    /**
     * Creates a new [FunctionTypeDefBuilder] for defining a function typedef with the given name.
     *
     * @param name the name of the typedef
     * @return a new [FunctionTypeDefBuilder] instance
     */
    fun function(name: String): FunctionTypeDefBuilder =
        FunctionTypeDefBuilder(ClassName(name))

    /**
     * Creates a new [FunctionTypeDefBuilder] for defining a function typedef with type parameters.
     *
     * @param name the name of the typedef
     * @param typeCasts the generic type parameters of the function typedef
     * @return a new [FunctionTypeDefBuilder] instance
     */
    fun function(
        name: String,
        vararg typeCasts: TypeName
    ): FunctionTypeDefBuilder =
        FunctionTypeDefBuilder(ClassName(name).parameterizedBy(*typeCasts))

    /**
     * Creates a new [FunctionTypeDefBuilder] for defining a function typedef with type parameters.
     *
     * @param typeName the type of the function typedef
     * @return a new [FunctionTypeDefBuilder] instance
     */
    fun function(
        typeName: TypeName
    ): FunctionTypeDefBuilder =
        FunctionTypeDefBuilder(typeName)
}
