package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.ALLOWED_PRIMITIVE_TYPES
import net.theevilreaper.dartpoet.util.NULLABLE_CHAR
import kotlin.reflect.KClass

/**
 * A class representing a custom type named [ClassName].
 *
 * @param name the name of the [ClassName]
 * @param isNullable a flag indicating whether the [ClassName] can be null (default is false).
 */
open class ClassName(
    val name: String,
    isNullable: Boolean = false
) : TypeName(isNullable) {

    init {
        require(name.trim().isNotEmpty()) { "The name of a ClassName can't be empty (includes only spaces)" }
    }

    /**
     * Emits the name of the [ClassName] to a [CodeWriter].
     *
     * @param out the [CodeWriter] instance to which the name is emitted
     * @return the same [CodeWriter] instance for method chaining
     */
    override fun emit(out: CodeWriter): CodeWriter {
        out.emit(name)

        if (isNullable) {
            out.emit(NULLABLE_CHAR)
        }
        return out
    }

    /**
     * Creates a copy of the [ClassName] with an optional nullable flag.
     *
     * @param nullable a flag indicating whether the copied [ClassName] can be null
     * @return a new [ClassName] instance with the provided nullable flag
     */
    override fun copy(nullable: Boolean): TypeName {
        return ClassName(name, nullable)
    }
}

@JvmName("get")
fun KClass<*>.asClassName(): ClassName {
    val simpleName = this.simpleName!!
    if (this.simpleName in ALLOWED_PRIMITIVE_TYPES) {
        return TypeName.parseSimpleKClass(this)
    }

    if (this == Void::class) {
        return ClassName(Void::class.simpleName!!.replaceFirstChar { it.lowercase() })
    }
    return ClassName(simpleName)

}

@JvmName("get")
fun Class<*>.asClassName(): ClassName {
    require(!isPrimitive) { "A primitive type can't be represented over a ClassName!" }
    require(Void.TYPE != this) { "A void type can't be represented over a ClassName!" }
    require(!isArray) { "An array can't be represented over a ClassName!" }
    return ClassName(this.simpleName)
}


