package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import kotlin.reflect.KClass

class ClassName(
    val name: String,
    isNullable: Boolean = false
) : TypeName(isNullable) {

    override fun emit(out: CodeWriter): CodeWriter = out.emit(name)

    override fun copy(nullable: Boolean): TypeName {
        return ClassName(name, nullable)
    }

    companion object {

        fun bestGuess(className: String): ClassName {
            val names = mutableListOf<String>()

            var position = 0

            while (position < className.length && Character.isLowerCase(className.codePointAt(position))) {
                position = className.indexOf(".", position) + 1
                require(position != 0) { "NOPE" }
            }

            names += if (position != 0) className.substring(0, position - 1) else ""

            for (part in className.substring(position).split(".")) {
                require(part.isNotEmpty() && Character.isUnicodeIdentifierPart(part.codePointAt(0))) {
                    "NOPE"
                }

                names += part
            }

            return ClassName(names[names.size - 1].lowercase())
        }
    }
}

@JvmName("get")
fun KClass<*>.asClassName(): TypeName {
    if (this.java.isPrimitive) {
        return TypeName.get(this.java)
    }
    qualifiedName.orEmpty().let { return ClassName.bestGuess(it) }
    throw IllegalArgumentException("NOPE")
}

@JvmName("get")
fun Class<*>.asClassName(): ClassName {
    require(!isPrimitive) { "A primitive type can't be represented over a ClassName!" }
    require(Void.TYPE != this) { "NOPE" }
    require(!isArray) { "NOPE" }

    val names = mutableListOf<String>()

    var c = this

    while (true) {
        names += c.simpleName
        val enclosing = c.enclosingClass ?: break
        c = enclosing
    }

    val lastDot = c.name.lastIndexOf(".")
    if (lastDot != -1) names += c.name.substring(0, lastDot)
    names.reverse()
    return ClassName(names[0])
}
