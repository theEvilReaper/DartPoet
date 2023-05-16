package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.SEMICOLON

class ExportDirective(
    private val path: String
): BaseDirective(path) {
    override fun write(writer: CodeWriter) {
        writer.emit("export·")
        writer.emit("'")
        writer.emit(path.ensureDartFileEnding())
        writer.emit("'")
        writer.emit(SEMICOLON)
    }
}
