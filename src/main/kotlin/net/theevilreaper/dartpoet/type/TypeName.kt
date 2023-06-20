package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import java.lang.reflect.Type
import kotlin.reflect.KClass

sealed class TypeName(
    private val isNullable: Boolean
) {

    abstract fun copy(nullable: Boolean = this.isNullable) : TypeName

    internal abstract fun emit(out: CodeWriter): CodeWriter

    internal fun emitNullable(out: CodeWriter) {
        if (isNullable) {
            out.emit("?")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return toString() == other.toString()
    }

    override fun hashCode(): Int = toString().hashCode()

    companion object {
        internal fun get(type: Type): TypeName {
            return when(type) {
                is Class<*> -> when {
                    type === Boolean::class.javaPrimitiveType -> BOOLEAN
                    type === Int::class.javaPrimitiveType -> INT
                    type === String::class.javaPrimitiveType -> STRING
                    type === Double::class.javaPrimitiveType -> DOUBLE
                    else -> type.asClassName()
                }
                else -> throw Error("What")
            }
        }
    }
}

@JvmField val BOOLEAN: ClassName = ClassName("bool")

@JvmField val INT: ClassName = ClassName("int")

@JvmField val DOUBLE: ClassName = ClassName("double")

@JvmField val STRING: ClassName = ClassName("String")

@JvmName("get")
fun Type.asTypeName() : TypeName = TypeName.get(this)

@JvmName("get")
fun KClass<*>.asTypeName(): ClassName = asClassName()
