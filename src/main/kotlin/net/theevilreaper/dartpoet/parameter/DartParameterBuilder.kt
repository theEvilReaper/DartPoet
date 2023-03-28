package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.code.CodeFragment
import net.theevilreaper.dartpoet.code.CodeFragmentBuilder

class DartParameterBuilder internal constructor(
    val name: String,
    val type: String,
) {

    internal var named: Boolean = false
    internal var required: Boolean = false
    internal var nullable: Boolean = false
    //internal val initializer: CodeFragmentBuilder = CodeFragment.builder()

    fun initializer(format: String, vararg args: Any) = apply {
        //this.initializer.add(format, args)
    }

    fun named(named: Boolean) = apply {
        this.named = named
    }

    fun nullable(nullable: Boolean) = apply {
        this.nullable = nullable
    }

    fun required(required: Boolean) = apply {
        this.required = required
    }

    fun build(): DartParameterSpec {
        return DartParameterSpec(this)
    }


}