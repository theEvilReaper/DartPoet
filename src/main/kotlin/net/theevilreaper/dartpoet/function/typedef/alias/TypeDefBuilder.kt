package net.theevilreaper.dartpoet.function.typedef.alias

import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName
import java.util.function.Function
import kotlin.reflect.KClass

/**
 * The builder is used to create a type definition with a specific name and optional type cast.
 * After the construction the builder maps the data into a [TypeDefSpec] object.
 *
 * @param typeDefName the name of the type definition.
 * @param typeCasts optional array of type-cast for the type definition.
 */
open class TypeDefBuilder internal constructor(
    val typeDefName: String,
    val typeName: TypeName = Function::class.asTypeName(),
    val typeCasts: List<TypeName> = emptyList(),
) {

    /**
     * The return type of the type definition.
     */
    var returnType: TypeName = Void::class.asTypeName()

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
    open fun build(): AbstractTypeDef<*> = TypeDefSpec(this)
}
