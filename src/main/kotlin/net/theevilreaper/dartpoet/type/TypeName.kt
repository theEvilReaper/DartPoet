package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import java.lang.reflect.Type
import kotlin.reflect.KClass

sealed class TypeName(val isNullable: Boolean) {

    private val cachedString: String by lazy {
        buildCodeString {
            emit(this)
            if (isNullable) emit("?")
        }
    }


    override fun toString(): String = cachedString

    internal abstract fun emit(out: CodeWriter): CodeWriter
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TypeName

        return cachedString == other.cachedString
    }

    override fun hashCode(): Int = cachedString.hashCode()

    companion object {

        internal fun get(type: Type): TypeName {
            return when(type) {
                is Class<*> -> when {
                    type === Boolean::class.javaPrimitiveType -> BOOLEAN
                    type === Int::class.javaPrimitiveType -> INTEGER
                    type === Long::class.javaPrimitiveType -> INTEGER
                    type === Double::class.javaPrimitiveType -> DOUBLE
                    type === Float::class.javaPrimitiveType -> DOUBLE
                    type === String::class.javaPrimitiveType -> STRING
                    type.isArray -> throw IllegalArgumentException("An array type is not supported at the moment")
                    else -> type.asClassName()
                }

                else -> throw IllegalArgumentException("Received unexpected type $type")

            }
        }
    }
}

@JvmField val BOOLEAN: ClassName = ClassName("bool")

@JvmField val INTEGER: ClassName = ClassName("int")

@JvmField val DOUBLE: ClassName = ClassName("double")

@JvmField val STRING: ClassName = ClassName("String")

@JvmName("get")
fun KClass<*>.asTypeName(): TypeName = asClassName()

@JvmName("get")
fun Type.asClassName(): TypeName = TypeName.get(this)
