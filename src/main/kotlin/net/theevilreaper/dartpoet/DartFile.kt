package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.DartFileWriter
import net.theevilreaper.dartpoet.directive.*
import net.theevilreaper.dartpoet.extension.ExtensionSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.util.*
import net.theevilreaper.dartpoet.util.ALLOWED_CONST_MODIFIERS
import net.theevilreaper.dartpoet.property.PropertySpec
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
    internal val docs = builder.docs

    internal val constants: Set<PropertySpec> = builder.constants.onEach {
        // Only check modifiers when the size is not zero
        if (it.modifiers.isNotEmpty()) {
            hasAllowedModifiers(it.modifiers, ALLOWED_CONST_MODIFIERS, "file const")
        }
    }.toImmutableSet()

    private val importList: List<Directive> = builder.directives.toList()

    internal val dartDirectives: List<DartDirective> = importList.filterAndSort<DartDirective> { it.toString() }

    internal val relativeImports: List<RelativeDirective> = importList.filterAndSort<RelativeDirective> { it.toString() }

    internal val partDirectives: List<PartDirective> = importList.filterAndSort<PartDirective> { it.toString() }

    internal val libImport: LibraryDirective? = importList.filterAndSort<LibraryDirective> { it.toString() }.let {
        if (it.size > 1) {
            throw Exception("Only one library import is allowed")
        }
        if (it.isEmpty()) { null } else { it.first() }
    }

    internal val exports: List<ExportDirective> = importList.filterAndSort<ExportDirective> { it.toString() }

    val hasImports get() = dartDirectives.isNotEmpty() || partDirectives.isNotEmpty() || libImport != null

    private val hasOnlyImports get() = dartDirectives.isNotEmpty() || partDirectives.isNotEmpty() || relativeImports.isNotEmpty()

    val emitTrailingDirective = hasOnlyImports && constants.isEmpty()

    init {
        check(name.trim().isNotEmpty()) { "The name of a class can't be empty (ONLY SPACES ARE NOT ALLOWED" }
    }

    internal fun write(codeWriter: CodeWriter) { DartFileWriter().emit(this, codeWriter) }

    override fun toString() = buildCodeString { write(this) }

    internal val callEmit: (Any, CodeWriter) -> Unit = {
        o: Any, c: CodeWriter -> emitInternal(o, c)
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

    companion object {

        @JvmStatic
        fun builder(name: String): DartFileBuilder {
            return DartFileBuilder(name)
        }
    }
}