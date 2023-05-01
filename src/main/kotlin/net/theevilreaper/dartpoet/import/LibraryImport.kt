package net.theevilreaper.dartpoet.import

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.util.SEMICOLON

class LibraryImport(
    private val path: String,
    private val asPartOf: Boolean = false
): Import {
    init {
        check(path.trim().isNotEmpty()) { "The path of an LibraryImport can't be empty" }
    }

    private val partImport: String = buildString {
        if (asPartOf) {
            append("part of ")
        } else {
            append("${DartModifier.LIBRARY.identifier} ")
        }
        append(path)
        append(SEMICOLON)
    }

    override fun toString(): String = partImport

    override fun compareTo(other: Import): Int = partImport.compareTo(other.toString())
}