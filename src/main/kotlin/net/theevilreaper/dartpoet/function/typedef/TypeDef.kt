package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.function.typedef.alias.TypeDefBuilder
import net.theevilreaper.dartpoet.function.typedef.function.FunctionTypeDefBuilder
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName

/**
 * THe [TypeDef] is a factory class to create different instance of a [net.theevilreaper.dartpoet.function.typedef.alias.TypeDefBuilder] to create type def declerations
 *
 * @since 1.0.0
 * @author theEvilReaper
 **/
object TypeDef {

    /**
     * Creates a new [net.theevilreaper.dartpoet.function.typedef.alias.TypeDefBuilder] for defining an alias typedef with the given name.
     *
     * @param name the name of the typedef
     * @return a new [net.theevilreaper.dartpoet.function.typedef.alias.TypeDefBuilder] instance
     */
    fun alias(name: String): TypeDefBuilder = TypeDefBuilder(name)

    /**
     * Creates a new [TypeDefBuilder] for defining an alias typedef with the given type.
     *
     * @param name the name of the typedef
     * @param typeName the aliased type
     * @return a new [TypeDefBuilder] instance
     */
    fun alias(name: String, typeName: TypeName): TypeDefBuilder =
        TypeDefBuilder(name, typeName)

    /**
     * Creates a new [TypeDefBuilder] for defining an alias typedef with the given type name.
     *
     * @param name the name of the typedef
     * @param typeName the aliased type as a string
     * @return a new [TypeDefBuilder] instance
     */
    fun alias(name: String, typeName: String): TypeDefBuilder =
        TypeDefBuilder(name, ClassName(typeName))

    /**
     * Creates a new [TypeDefBuilder] for defining an alias typedef with type parameters.
     *
     * @param name the name of the typedef
     * @param typeName the aliased type as a string
     * @param typeCasts the generic type parameters of the typedef
     * @return a new [TypeDefBuilder] instance
     */
    fun alias(
        name: String,
        typeName: String,
        vararg typeCasts: TypeName
    ): TypeDefBuilder =
        TypeDefBuilder(name, ClassName(typeName), typeCasts = typeCasts.toList())

    /**
     * Creates a new [FunctionTypeDefBuilder] for defining a function typedef with the given name.
     *
     * @param name the name of the typedef
     * @return a new [FunctionTypeDefBuilder] instance
     */
    fun function(name: String): FunctionTypeDefBuilder =
        FunctionTypeDefBuilder(name)

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
        FunctionTypeDefBuilder(name, typeCasts = typeCasts.toList())
}
