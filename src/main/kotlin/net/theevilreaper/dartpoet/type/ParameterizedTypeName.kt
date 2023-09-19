package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * Represents a parameterized type name, which includes information about the
 * raw type, its type arguments, and whether it is nullable.
 *
 * @param enclosingTypeName (Optional) The enclosing type name if this parameterized type is nested within another type
 * @param rawType The raw type of this parameterized type
 * @param typeArguments The list of type arguments associated with the raw type
 * @param nullable Specifies whether this parameterized type is nullable (default is false)
 *
 * @author theEvilReaper
 * @since 1.0.0
 */
class ParameterizedTypeName internal constructor(
    private val enclosingTypeName: TypeName?,
    private val rawType: ClassName,
    private val typeArguments: List<TypeName>,
    nullable: Boolean = false
) : TypeName(nullable) {

    init {
        require(typeArguments.isNotEmpty() || enclosingTypeName != null) {
            "no type arguments: $rawType"
        }
    }

    /**
     * Creates a copy of the [ParameterizedTypeName] with an optional nullable flag.
     *
     * @param nullable a flag indicating whether the copied [ParameterizedTypeName] can be null
     * @return a new [ParameterizedTypeName] instance with the provided nullable flag
     */
    override fun copy(nullable: Boolean): ParameterizedTypeName {
        return ParameterizedTypeName(enclosingTypeName, rawType, typeArguments, nullable)
    }

    /**
     * Creates a copy of the [ParameterizedTypeName] with an optional nullable flag.
     *
     * @param nullable a flag indicating whether the copied [ParameterizedTypeName] can be null
     * @param typeArguments (Optional) the list of type arguments for the new [ParameterizedTypeName].
     *                      If not provided, the type arguments from the current instance will be used
     *
     * @return a new [ParameterizedTypeName] instance with the provided nullable flag
     */
    fun copy(nullable: Boolean, typeArguments: List<TypeName> = this.typeArguments): ParameterizedTypeName {
        return ParameterizedTypeName(enclosingTypeName, rawType, typeArguments, nullable)
    }

    /**
     * Emits the structure of the [ParameterizedTypeName] to a [CodeWriter].
     *
     * @param out the [CodeWriter] instance to which the name is emitted
     * @return the same [CodeWriter] instance for method chaining
     */
    override fun emit(out: CodeWriter): CodeWriter {
        if (enclosingTypeName != null) {
            enclosingTypeName.emit(out)
            out.emit("." + rawType.name)
        } else {
            rawType.emit(out)
        }

        if (typeArguments.isNotEmpty()) {
            out.emit("<")
            typeArguments.forEachIndexed { index, typeName ->
                if (index > 0) out.emit(", ")
                typeName.emit(out)
            }
            out.emit(">")
        }
        return out
    }

    companion object {

        @JvmStatic
        @JvmName("get")
        fun ClassName.parameterizedBy(
            vararg typeArguments: TypeName,
        ): ParameterizedTypeName = ParameterizedTypeName(null, this, typeArguments.toList())

        @JvmStatic
        @JvmName("get")
        fun ClassName.parameterizedBy(
            vararg typeArguments: KClass<*>,
        ): ParameterizedTypeName = ParameterizedTypeName(null, this, typeArguments.map { it.asTypeName() })

        @JvmStatic
        @JvmName("get")
        fun Class<*>.parameterizedBy(
            vararg typeArguments: Type,
        ): ParameterizedTypeName =
            ParameterizedTypeName(null, asClassName(), typeArguments.map { it.asTypeName() })

        @JvmStatic
        @JvmName("get")
        fun KClass<*>.parameterizedBy(
            vararg typeArguments: KClass<*>
        ): ParameterizedTypeName =
            ParameterizedTypeName(null, asClassName(), typeArguments.map { it.asTypeName() })

        @JvmStatic
        @JvmName("get")
        fun KClass<*>.parameterizedBy(
            vararg typeArguments: TypeName
        ): ParameterizedTypeName =
            ParameterizedTypeName(null, asClassName(), typeArguments.map { it })
    }
}