package net.theevilreaper.dartpoet.import

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.SEMICOLON

class RelativeDirective(
    private val path: String,
    private val castType: CastType? = null,
    private val importCast: String? = null,
): BaseDirective(path) {

    override fun write(writer: CodeWriter) {
        writer.emit("import ")
        writer.emit("'")
        writer.emit(path)
        writer.emit("'")

        if (castType != null && importCast != null) {
            writer.emit(" ${castType.identifier} $importCast")
        }

        writer.emit(SEMICOLON)
    }
}
