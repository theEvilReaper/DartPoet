package net.theevilreaper.dartpoet.file

import net.theevilreaper.dartpoet.DartFileBuilder
import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.TypeName

/**
 * The [FileConstantBuilder] can be used to construct new [FileConstantSpec] object reference which can be set
 * into a [DartFileBuilder].
 * @author theEvilReaper
 * @since 1.0.0
 */
class FileConstantBuilder internal constructor(
    val name: String,
    val typeName: TypeName? = null
) {
    internal var initializer: CodeBlock.Builder = CodeBlock.Builder()

    /**
     * Apply a given format which contains the parts for the init block of the [PropertySpec].
     * @param format the given format
     * @param args the arguments for the format
     */
    fun initWith(format: String, vararg args: Any?) = apply {
        this.initializer.add(format, *args)
    }

    /**
     * Set the initializer block directly as [CodeBlock.Builder] to the property.
     * @param codeFragment the [CodeBlock.Builder] to set
     */
    fun initWith(codeFragment: CodeBlock.Builder) = apply {
        this.initializer = codeFragment
    }

    /**
     * Constructs a [FileConstantSpec] reference from the current builder instance
     * @return the created [FileConstantSpec] instance
     */
    fun build(): FileConstantSpec {
        return FileConstantSpec(this)
    }
}
