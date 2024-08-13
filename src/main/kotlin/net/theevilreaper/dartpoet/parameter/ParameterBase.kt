package net.theevilreaper.dartpoet.parameter

open class ParameterBase(
    val name: String,
    val type: ParameterType
) {
    init {
        check(name.trim().isNotEmpty()) { "The name of the parameter must not be empty" }
    }
}