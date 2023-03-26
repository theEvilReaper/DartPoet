package net.theevilreaper.dartpoet.parameter

class DartParameterSpec internal constructor(
    private val builder: DartParameterBuilder
) {

    val name = builder.name
    val type = builder.type
    val named = builder.named



}