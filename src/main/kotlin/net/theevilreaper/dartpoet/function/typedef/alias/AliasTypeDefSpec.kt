package net.theevilreaper.dartpoet.function.typedef.alias

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.function.typedef.AbstractTypeDef
import net.theevilreaper.dartpoet.util.SEMICOLON

/**
 * The class models a typedef from dart into a structure which can be used to generate and organize such methods.
 * For more details visit the documentation from dart
 * @param builder the builder instance to retrieve the data from
 * @see <a href="https://dart.dev/language/typedefs">Dart Typedefs</a>.
 **/
class AliasTypeDefSpec internal constructor(
    val builder: AliasTypeDefBuilder
): AbstractTypeDef<AliasTypeDefBuilder>(
    builder.name,
) {
    internal val returnType = builder.returnType

    /**
     * Writes the right-hand side of a typedef declaration.
     * @param writer the writer instance
     */
    override fun writeRightHandSide(writer: CodeWriter) {
        writer.emitCode("%T", returnType)
        writer.emitCode(SEMICOLON)
    }

    /**
     * Converts a given instance of a [AliasTypeDefSpec] into a [AliasTypeDefBuilder].
     * This is useful if you want to modify an existing spec object.
     * @return the created builder
     */
    override fun toBuilder(): AliasTypeDefBuilder {
        val newBuilder = AliasTypeDefBuilder(this.type)
        newBuilder.returnType = this.returnType
        return newBuilder
    }
}
