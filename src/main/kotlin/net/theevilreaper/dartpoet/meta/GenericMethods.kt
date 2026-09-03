package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * Defines builder methods for declarations that support generic type parameters.
 *
 * Implemented by builders such as `ClassBuilder`, `EnumBuilder`, and `MixinBuilder`
 * to offer a consistent, fluent API for configuring type parameters with or without bounds.
 *
 * @param T the concrete builder type for fluent method chaining
 * @author theEvilReaper
 * @since 2.4.0
 */
interface GenericMethods<T> {

    /**
     * Adds a generic type parameter as a [TypeName].
     *
     * @param typeName the type name to add
     * @return this builder instance
     */
    fun genericCast(typeName: TypeName): T

    /**
     * Adds multiple generic type parameters as [TypeName] instances.
     *
     * @param typeNames the type names to add
     * @return this builder instance
     */
    fun genericCasts(vararg typeNames: TypeName): T

    /**
     * Adds an unconstrained generic type parameter, such as `<T>`.
     *
     * @param name the type parameter name (e.g. "T", "Item", or "Result")
     * @return this builder instance
     */
    fun generic(name: String): T

    /**
     * Adds a generic type parameter from a [ClassName].
     *
     * @param type the class name representing the type parameter
     * @return this builder instance
     */
    fun generic(type: ClassName): T

    /**
     * Adds a generic type parameter from a Java reflection [Type].
     *
     * @param type the reflection type to convert and add
     * @return this builder instance
     */
    fun generic(type: Type): T

    /**
     * Adds a generic type parameter from a Kotlin [KClass].
     *
     * @param type the Kotlin class to convert and add
     * @return this builder instance
     */
    fun generic(type: KClass<*>): T

    /**
     * Adds a generic type parameter from a Java [Class].
     *
     * @param type the Java class to convert and add
     * @return this builder instance
     */
    fun generic(type: Class<*>): T

    /**
     * Adds a bounded generic type parameter, such as `<T extends Comparable>`.
     *
     * @param name the name of the type parameter
     * @param bound the upper bound that the type parameter must satisfy
     * @return this builder instance
     */
    fun generic(name: String, bound: TypeName): T

    /**
     * Adds a bounded generic type parameter using a [ClassName] bound.
     *
     * @param name the name of the type parameter
     * @param bound the upper bound as a [ClassName]
     * @return this builder instance
     */
    fun generic(name: String, bound: ClassName): T = generic(name, bound as TypeName)

    /**
     * Adds a bounded generic type parameter using a Kotlin [KClass] bound.
     *
     * @param name the name of the type parameter
     * @param bound the upper bound as a Kotlin class
     * @return this builder instance
     */
    fun generic(name: String, bound: KClass<*>): T = generic(name, bound.asTypeName())

    /**
     * Adds a bounded generic type parameter using a Java [Class] bound.
     *
     * @param name the name of the type parameter
     * @param bound the upper bound as a Java class
     * @return this builder instance
     */
    fun generic(name: String, bound: Class<*>): T = generic(name, bound.asClassName())
}
