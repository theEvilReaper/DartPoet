package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.DartModifier

class ConstClassName : ClassName(DartModifier.CONST.identifier) {

    @Throws(IllegalAccessException::class)
    override fun copy(nullable: Boolean): TypeName {
        throw IllegalAccessException("The dynamic type can't be copied")
    }
}