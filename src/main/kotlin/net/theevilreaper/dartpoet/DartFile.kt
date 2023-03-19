package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.writer.CodeWriter
import java.io.IOException
import java.lang.Appendable
import java.nio.file.Path

class DartFile internal constructor(
    builder: DartFileBuilder
) {

    internal val annotations: List<AnnotationSpec> = builder.specData.annotations.toImmutableList()
    internal val documentation: List<String> = builder.documentation.toImmutableList()

    @Throws(IOException::class)
    fun write(out: Appendable) {
        val codeWriter = CodeWriter(
            out
        )
        codeWriter.close()
    }

    @Throws(IOException::class)
    fun write(path: Path) {
    }

    companion object {

        @JvmStatic
        fun builder(name: String): DartFileBuilder {
            return DartFileBuilder(name)
        }
    }
}