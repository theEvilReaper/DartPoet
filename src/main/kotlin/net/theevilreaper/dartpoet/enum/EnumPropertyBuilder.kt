package net.theevilreaper.dartpoet.enum

import net.theevilreaper.dartpoet.code.CodeBlock

class EnumPropertyBuilder(
    val name: String
) {
    internal var genericValueCast: String? = null
    internal val parameters: MutableList<CodeBlock> = mutableListOf()

    fun parameter(format: String, vararg args: Any) = apply {
        parameter(CodeBlock.of(format, *args))
    }

    fun parameter(block: CodeBlock) = apply {
        this.parameters += block
    }

    fun generic(value: String) = apply {
        this.genericValueCast = value
    }

    fun build(): EnumPropertySpec {
        return EnumPropertySpec(this)
    }
}
