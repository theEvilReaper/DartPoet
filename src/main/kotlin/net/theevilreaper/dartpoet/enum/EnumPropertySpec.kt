package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.EnumPropertyWriter
import net.theevilreaper.dartpoet.util.toImmutableList

class EnumPropertySpec(
    val builder: EnumPropertyBuilder
) {

    internal val name = builder.name
    internal val generic = builder.genericValueCast
    internal val hasGeneric = builder.genericValueCast.orEmpty().isNotEmpty()
    internal val parameters = builder.parameters.toImmutableList()
    internal val hasParameter = builder.parameters.isNotEmpty()

    init {
        check(name.trim().isNotEmpty()) { "The name of a EnumProperty can't be empty" }
        if (generic != null && hasGeneric) {
            check(generic.trim().isNotEmpty()) { "The generic cast can't be empty" }
        }
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        EnumPropertyWriter().write(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this,
        )
    }

    fun toBuilder(): EnumPropertyBuilder {
        val builder = EnumPropertyBuilder(name)
        builder.genericValueCast = generic
        builder.parameters += parameters
        return builder
    }

    companion object {

        @JvmStatic
        fun builder(name: String) = EnumPropertyBuilder(name)
    }
}
