package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import java.lang.reflect.Type
import kotlin.reflect.KClass

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

    override fun copy(nullable: Boolean): ParameterizedTypeName {
        return ParameterizedTypeName(enclosingTypeName, rawType, typeArguments, nullable)
    }

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