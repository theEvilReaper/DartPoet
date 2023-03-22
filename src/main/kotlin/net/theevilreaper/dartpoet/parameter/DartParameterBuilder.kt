package net.theevilreaper.dartpoet.parameter

class DartParameterBuilder internal constructor(
    val name: String,
    val type: String,
) {

    internal var named: Boolean = false

    fun named(named: Boolean) = apply {
       this.named = named
    }

    fun build(): DartParameterSpec {
        return DartParameterSpec(this)
    }


}