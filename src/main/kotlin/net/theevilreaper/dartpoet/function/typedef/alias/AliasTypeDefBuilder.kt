package net.theevilreaper.dartpoet.function.typedef.alias

import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import kotlin.reflect.KClass

/**
 * The builder is used to create a type definition with a specific name and optional type cast.
 * After the construction the builder maps the data into a [AliasTypeDefSpec] object.
 *
 * @param name the name of the type definition.
 */
class AliasTypeDefBuilder internal constructor(val name: TypeName) {

    /**
     * The return type of the type definition.
     */
    var returnType: TypeName? = null

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
     * Builds and returns an instance of [AliasTypeDefSpec] based on the configuration.
     *
     * @return an instance of [AliasTypeDefSpec].
     */
    fun build(): AbstractTypeDef<*> = AliasTypeDefSpec(this)
}
