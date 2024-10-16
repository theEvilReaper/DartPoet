package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.directive.impl.DartDirective
import net.theevilreaper.dartpoet.directive.impl.ExportDirective
import net.theevilreaper.dartpoet.directive.impl.LibraryDirective
import net.theevilreaper.dartpoet.directive.impl.PartDirective
import net.theevilreaper.dartpoet.directive.impl.RelativeDirective
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.util.*
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
    internal val constants: Set<ConstantPropertySpec> = builder.constants.toImmutableSet()
    private val directives = builder.directives.toImmutableList()
    internal val dartImports =
        DirectiveOrdering.sortDirectives<DartDirective>(DartDirective::class, directives) { it.contains("dart:") }
    internal val packageImports =
        DirectiveOrdering.sortDirectives<DartDirective>(DartDirective::class, directives) { it.contains("package:") }
    internal val partImports = DirectiveOrdering.sortDirectives<PartDirective>(PartDirective::class, directives)
    internal val libImport = DirectiveOrdering.sortDirectives<LibraryDirective>(LibraryDirective::class, directives)
    internal val exportDirectives =
        DirectiveOrdering.sortDirectives<ExportDirective>(ExportDirective::class, directives)
    internal val relativeImports =
        DirectiveOrdering.sortDirectives<RelativeDirective>(RelativeDirective::class, directives)
    internal val typeDefs = builder.typeDefs.toImmutableList()
    internal val hasTypeDefs = typeDefs.isNotEmpty()

    init {
        check(name.trim().isNotEmpty()) { "The name of a class can't be empty (ONLY SPACES ARE NOT ALLOWED" }
        if (libImport.isNotEmpty()) {
            check(libImport.size == 1) { "Only one library directive is allowed" }
        }
    }

    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.fileWriter.write(this, codeWriter)
    }

    override fun toString() = buildCodeString { write(this) }

    /**
     * Writes the content from a [DartFile] to the given [Appendable].
     * @param out the [Appendable] where the content should be written
     * @throws IOException if the content can't be written
     */
    @Throws(IOException::class)
    fun write(out: Appendable) {
        val codeWriter = CodeWriter(
            out,
            indent = indent
        )
        write(codeWriter)
        codeWriter.close()
    }

    /**
     * Writes the content from a [DartFile] to the given [Path].
     * @param path the path where the file should be written
     * @throws IOException if the file can't be written
     */
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

    /**
     * Creates a new instance of [DartFileBuilder] with the specified name.
     */
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