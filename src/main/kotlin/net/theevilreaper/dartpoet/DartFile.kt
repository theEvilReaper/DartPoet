package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.DartFileWriter
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.directive.DartDirective
import net.theevilreaper.dartpoet.directive.LibraryDirective
import net.theevilreaper.dartpoet.directive.PartDirective
import net.theevilreaper.dartpoet.property.consts.ConstantPropertySpec
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
    internal val types: List<ClassSpec> = builder.specTypes.toImmutableList()
    internal val extensions: List<ExtensionSpec> = builder.extensionStack
    internal val docs = builder.docs
    private val directives = builder.directives.toImmutableList()
    internal val constants: Set<ConstantPropertySpec> = builder.constants.toImmutableSet()

    internal val imports: List<DartDirective> = if (directives.isEmpty()) {
        emptyList()
    } else {
        builder.directives.filterIsInstance<DartDirective>().toList()
    }

    internal val partImports: List<PartDirective> = if (directives.isEmpty()) {
        emptyList()
    } else {
        builder.directives.filterIsInstance<PartDirective>().toList()
    }

    internal val libImport: LibraryDirective? = if (directives.isEmpty()) {
        null
    } else {
        val possibleListImports = directives.filterIsInstance<LibraryDirective>()
        if (possibleListImports.isEmpty()) {
            null
        } else if (possibleListImports.size == 1) {
            possibleListImports.first()
        } else {
            throw Exception("Only one library import is allowed")
        }
    }

    init {
        check(name.trim().isNotEmpty()) { "The name of a class can't be empty (ONLY SPACES ARE NOT ALLOWED" }
    }

    internal fun write(codeWriter: CodeWriter) {
        DartFileWriter().write(this, codeWriter)
    }

    override fun toString() = buildCodeString { write(this) }

    internal val callEmit: (Any, CodeWriter) -> Unit = { o: Any, c: CodeWriter ->
        emitInternal(o, c)
    }

    private fun emitInternal(o: Any, c: CodeWriter) {
        when (o::class) {
            FunctionSpec::class -> {
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

        val fileName = if (name.endsWith(DART_FILE_ENDING)) {
            name
        } else {
            "$name$DART_FILE_ENDING"
        }

        val outPutPath = path.resolve(fileName)
        OutputStreamWriter(Files.newOutputStream(outPutPath), Charsets.UTF_8)
            .use { writer -> write(writer) }
    }

    /**
     * Converts a [DartFile] to a [DartFileBuilder] instance.
     * @return the created [DartFileBuilder] instance
     */
    fun toBuilder(): DartFileBuilder {
        val builder = DartFileBuilder(this.name)
        builder.specTypes.addAll(this.types)
        builder.directives.addAll(this.directives)
        builder.annotations.addAll(this.annotations)
        builder.extensionStack.addAll(this.extensions)
        builder.constants.addAll(this.constants)
        builder.docs.addAll(this.docs.toMutableList())
        builder.indent = this.indent
        return builder
    }

    companion object {

        /**
         * Creates a new instance from an [DartFileBuilder] to create class structures.
         * @param name the name which is used for the dart file
         * @return the created instance from the [DartFileBuilder] instance
         */
        @JvmStatic
        fun builder(name: String): DartFileBuilder {
            return DartFileBuilder(name)
        }
    }
}