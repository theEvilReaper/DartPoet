package net.theevilreaper.dartpoet.function.typedef

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.code.WriterHelper
import net.theevilreaper.dartpoet.code.buildCodeString
import net.theevilreaper.dartpoet.code.writer.FunctionWriter
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asTypeName

/**
 * The class models a typedef from dart into a structure which can be used to generate and organize such methods.
 * For more details visit the documentation from dart
 * @param builder the builder instance to retrieve the data from
 * @see <a href="https://dart.dev/language/typedefs">Dart Typedefs</a>.
 */
open class TypeDefSpec(
    val builder: TypeDefBuilder<*>
) {
    internal val name = builder.typeName
    internal val typeDefName = builder.typeDefName
    internal val typeName = builder.typeName
    internal val typeCasts = builder.typeCasts
    internal val returnType = builder.returnType ?: Void::class.asTypeName()

    /**
     * Performs some checks to avoid invalid data.
     */
    init {
        require(typeDefName.trim().isNotEmpty()) { "The name of a typedef can't be empty" }
    }

    /**
     * Calls a [FunctionWriter] to append the content from a spec object to a [CodeWriter].
     * @param codeWriter the writer instance
     */
    internal fun write(codeWriter: CodeWriter) {
        WriterHelper.typeDefWriter.write(this, codeWriter)
    }

    /**
     * Creates a textual representation from the spec object.
     * @return the spec object as string
     */
    override fun toString() = buildCodeString { write(this) }

    /**
     * Converts a given instance of a [TypeDefSpec] into a [TypeDefBuilder].
     * This is useful if you want to modify an existing spec object.
     * @return the created builder
     */
    open fun toBuilder(): TypeDefBuilder<*> {
        val newBuilder = TypeDefBuilder(this.typeDefName, this.typeName, *this.typeCasts)
        newBuilder.returnType = this.returnType
        return newBuilder
    }

    /**
     * The companion object contains some helper methods to create a new instance of a [TypeDefSpec].
     */
    companion object {

        @JvmStatic
        fun alias(name: String): TypeDefBuilder<*> = TypeDefBuilder(name)

        @JvmStatic
        fun alias(name: String, typeName: TypeName): TypeDefBuilder<*> = TypeDefBuilder(name, typeName)

        @JvmStatic
        fun alias(name: String, typeName: String): TypeDefBuilder<*> = TypeDefBuilder(name, ClassName(typeName))

        @JvmStatic
        fun alias(name: String, typeName: String, vararg typeCasts: TypeName): TypeDefBuilder<*> =
            FunctionTypeDefBuilder(name, ClassName(typeName), typeCasts = typeCasts)

        @JvmStatic
        fun function(name: String): TypeDefBuilder<*> = FunctionTypeDefBuilder(name)

        @JvmStatic
        fun function(name: String, vararg typeCasts: TypeName): FunctionTypeDefBuilder =
            FunctionTypeDefBuilder(name, typeCasts = typeCasts)
    }
}