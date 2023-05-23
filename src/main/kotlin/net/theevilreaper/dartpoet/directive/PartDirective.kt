package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.SEMICOLON

class PartDirective(
    private val path: String
) : BaseDirective(path) {

    override fun write(writer: CodeWriter) {
        writer.emit("part ")
        writer.emit("'")
        writer.emit(path)
        writer.emit("'$SEMICOLON")
    }
}
