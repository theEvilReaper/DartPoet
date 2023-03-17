package net.theevilreaper.dartpoet

enum class DartClassType(
    val keyWord: String,
    val defaultModifier: Array<DartModifier>
) {
    CLASS("class", arrayOf()),
    ENUM("enum", arrayOf()),
    MIXIN("mixin", arrayOf()),
}