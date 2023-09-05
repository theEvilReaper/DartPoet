package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter

class ParameterizedTypeName internal constructor(
    private val enclosingTypeName: TypeName?,
    val rawType: ClassName,
    val typeArguments: List<TypeName>,
    nullable: Boolean
): TypeName(nullable) {

    init {
        require(typeArguments.isNotEmpty() || enclosingTypeName != null) {
            "no type arguments: $rawType"
        }
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
            out.emit("<")
        }
        return out
    }

    companion object {


    }
}