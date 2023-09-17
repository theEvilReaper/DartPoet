package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.NULLABLE_CHAR
import kotlin.reflect.KClass

open class ClassName(
    val name: String,
    isNullable: Boolean = false
) : TypeName(isNullable) {

    init {
        require(name.trim().isNotEmpty()) { "The name of a ClassName can't be empty (includes only spaces)" }
    }

    override fun emit(out: CodeWriter): CodeWriter {
        out.emit(name)

        if (isNullable) {
            out.emit(NULLABLE_CHAR)
        }
        return out
    }

    override fun copy(nullable: Boolean): TypeName {
        return ClassName(name, nullable)
    }
}

@JvmName("get")
fun KClass<*>.asClassName(): ClassName {
    val simpleName = this.simpleName!!
    val simpleTypes = setOf("Byte", "Short", "Int", "Long", "Float", "Double", "Char", "Boolean")
    if (this.simpleName in simpleTypes) {
        return TypeName.parseSimpleClass(this)
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


