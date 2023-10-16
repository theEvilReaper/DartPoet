package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.DartModifier

internal interface VariableAppender {

    fun ensureVariableNameWithPrivateModifier(withPrivate: Boolean, name: String): String {
        require(name.trim().isNotEmpty()) { "The name parameter can't be empty" }
        return if (!withPrivate) name else "${DartModifier.PRIVATE.identifier}$name"
    }
}