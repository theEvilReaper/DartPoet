package net.theevilreaper.dartpoet.import

import net.theevilreaper.dartpoet.util.SEMICOLON

class PartImport internal constructor(
    private val path: String
) : Import {

    init {
        check(path.trim().isNotEmpty()) { "The path of an Import can't be empty" }
    }

    private val partImport: String = buildString {
        append("part ")
        append("'")
        append(path)
        append("'$SEMICOLON")
    }

    override fun toString(): String = partImport

    override fun compareTo(other: Import): Int = partImport.compareTo(other.toString())
}