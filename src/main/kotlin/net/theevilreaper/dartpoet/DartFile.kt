package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.import.DartImport
import net.theevilreaper.dartpoet.import.PartImport
import net.theevilreaper.dartpoet.method.DartFunctionSpec
import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.writer.CodeWriter
import java.io.IOException
import java.lang.Appendable
import java.nio.file.Path

class DartFile internal constructor(
    builder: DartFileBuilder,
) {

    internal val name: String = builder.name
    internal val indent: String = builder.indent
    internal val annotations: List<AnnotationSpec> = builder.annotations.toImmutableList()
    internal val specTypes: List<DartClassSpec> = builder.specTypes.toImmutableList()
    //internal val commentBlock: CodeFragmentBuilder = builder.comment

    internal val callEmit: (Any, CodeWriter) -> Unit = { o: Any, c: CodeWriter -> emitInternal(o, c) }

    private fun emitInternal(o: Any, c: CodeWriter) {
        when (o::class) {
            DartFunctionSpec::class -> {
                callEmit(o, c)
            }
        }
    }

    internal val imports: List<DartImport> = if (builder.imports.isEmpty()) {
        emptyList()
    } else {
        builder.imports.filterIsInstance<DartImport>().toList()
    }

    internal val partImports: List<PartImport> = if (builder.imports.isEmpty()) {
        emptyList()
    } else {
        builder.imports.filterIsInstance<PartImport>().toList()
    }

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