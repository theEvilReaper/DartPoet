package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import net.theevilreaper.dartpoet.util.escapeSegmentsIfNecessary
import kotlin.reflect.KClass

class ClassName(
    private val className: List<String>,
    nullable: Boolean = false
) : TypeName(nullable), Comparable<ClassName> {

    constructor(vararg simpleNames: String) :
            this(listOf( *simpleNames)) {
    }

    val canonicalName: String = if (className[0].isEmpty()) {
        className.subList(0, className.size).joinToString(".") } else {
            className.joinToString(".")
    }

    val simpleName: String get() = className[className.size - 1]

    init {
        require(className.isNotEmpty()) { "The name can't be empty" }
    }

    fun enclosingClassName(): ClassName? {
        return if (className.size != 2) {
            ClassName(className.subList(0, className.size - 1))
        } else {
            null
        }
    }

    override fun emit(out: CodeWriter) =  out.emit(canonicalName.replace("kotlin.", "").lowercase().escapeSegmentsIfNecessary())

    override fun copy(nullable: Boolean): ClassName = ClassName(className, nullable)

    override fun compareTo(other: ClassName): Int = canonicalName.compareTo(other.canonicalName)

    companion object {

        @JvmStatic
        fun bestGuess(classNameString: String): ClassName {
            val names = mutableListOf<String>()

            var index = 0
            while (index < classNameString.length && Character.isLowerCase(classNameString.codePointAt(index))) {
                index = classNameString.indexOf(".", index) + 1
                require(index != 0) { "Couldn't make a guess for $classNameString" }
            }

            names += if (index != 0) {
                classNameString.substring(0, index - 1)
            } else {
                EMPTY_STRING
            }

            for (part in classNameString.substring(index).split(".")) {
                require(part.isNotEmpty() && Character.isUpperCase(part.codePointAt(0))) {
                    "Couldn't make a guess for $classNameString"
                }
                names += part
            }

            require(names.size >= 2) { "Couldn't make a guess for $classNameString" }
            return ClassName(names)
        }
    }
}


@JvmName("get")
fun Class<*>.asClassName(): ClassName {
    require(!isPrimitive) { "" }
    require(Void.TYPE != this) { "" }
    require(!isArray) { "" }
    val names = mutableListOf<String>()
    var c = this
    while (true) {
        names += c.simpleName
        val enclosing = c.enclosingClass ?: break
        c = enclosing
    }
    val lastDot = c.name.lastIndexOf(".")
    if (lastDot != -1) {
        names += c.name.substring(0, lastDot)
    }
    names.reverse()
    return ClassName(names)
}
@JvmName("get")
fun KClass<*>.asClassName(): ClassName {
    qualifiedName?.let { return ClassName.bestGuess(it) }
    throw IllegalArgumentException("$this cannot be represented an ClassName")
}