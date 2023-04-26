package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.DartFileWriter
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.DartFunctionSpec
import net.theevilreaper.dartpoet.import.DartImport
import net.theevilreaper.dartpoet.import.LibraryImport
import net.theevilreaper.dartpoet.import.PartImport
import net.theevilreaper.dartpoet.util.DART_FILE_ENDING
import net.theevilreaper.dartpoet.util.isDartConventionFileName
import net.theevilreaper.dartpoet.util.toImmutableList
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.Appendable
import java.nio.file.Files
import java.nio.file.Path

class DartFile internal constructor(
    builder: DartFileBuilder
) {
    internal val name: String = builder.name
    internal val indent: String = builder.indent
    internal val annotations: List<AnnotationSpec> = builder.annotations.toImmutableList()
    internal val types: List<Any> = builder.specTypes.toImmutableList()
    internal val extensions: List<ExtensionSpec> = builder.extensionStack

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

    internal val libImport: LibraryImport? = if (builder.imports.isEmpty()) {
        null
    } else {
        builder.imports.filterIsInstance<LibraryImport>().let {
            null
        }
    }

    init {
        check(name.trim().isNotEmpty()) { "The name of a class can't be empty (ONLY SPACES ARE NOT ALLOWED" }
    }

    internal fun write(
        codeWriter: CodeWriter
    ) {
        DartFileWriter().emit(this, codeWriter)
    }

    override fun toString() = buildCodeString {
        write(
            this,
        )
    }

    internal val callEmit: (Any, CodeWriter) -> Unit = {
        o: Any, c: CodeWriter -> emitInternal(o, c)
    }

    private fun emitInternal(o: Any, c: CodeWriter) {
        when (o::class) {
            DartFunctionSpec::class -> {
                callEmit(o, c)
            }
        }
    }


    @Throws(IOException::class)
    fun write(out: Appendable) {
        val codeWriter = CodeWriter(
            out,
            indent = indent
        )
        write(codeWriter)
        codeWriter.close()
    }

    @Throws(IOException::class)
    fun write(path: Path) {
        require(Files.notExists(path) || Files.isDirectory(path)) {
            "The given path $path exists but it is not a directory"
        }

        require(isDartConventionFileName(name)) {
            """
             The given name $name has some issues with the naming   
             Please take a look at this page https://dart.dev/tools/linter-rules#file_names
            """.trimIndent()
        }

        val outPutPath = path.resolve("$name$DART_FILE_ENDING")
        OutputStreamWriter(Files.newOutputStream(outPutPath), Charsets.UTF_8)
            .use { writer -> write(writer) }
    }

    companion object {

        @JvmStatic
        fun builder(name: String): DartFileBuilder {
            return DartFileBuilder(name)
        }
    }
}