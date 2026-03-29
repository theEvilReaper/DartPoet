package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.util.COMMA_SEPARATOR
import net.theevilreaper.dartpoet.util.CURLY_CLOSE
import net.theevilreaper.dartpoet.util.CURLY_OPEN
import net.theevilreaper.dartpoet.util.NULLABLE_CHAR
import net.theevilreaper.dartpoet.util.StringHelper

class RecordTypeName(
    private val positionalFields: List<TypeName> = emptyList(),
    private val namedFields: List<Pair<String, TypeName>> = emptyList(),
    nullable: Boolean = false
) : TypeName(nullable) {

    /**
     * Performs some check to the given values from the constructor.
     */
    init {
        require(positionalFields.isNotEmpty() || namedFields.isNotEmpty()) {
            "no fields for record"
        }
    }

    /**
     * Creates a copy of the [RecordTypeName] with an optional nullable flag.
     *
     * @param nullable a flag indicating whether the copied [RecordTypeName] can be null
     * @return a new [RecordTypeName] instance with the provided nullable flag
     */
    override fun copy(nullable: Boolean): RecordTypeName {
        return RecordTypeName(positionalFields, namedFields, nullable)
    }

    /**
     * Emits the structure of the [ParameterizedTypeName] to a [CodeWriter].
     *
     * @param out the [CodeWriter] instance to which the name is emitted
     * @return the same [CodeWriter] instance for method chaining
     */
    override fun emit(out: CodeWriter): CodeWriter {
        out.emit("(")

        val joinedPositionalFields = StringHelper.concatData(
            positionalFields,
            separator = COMMA_SEPARATOR,
        ) { it.toString() }
        out.emit(joinedPositionalFields)

        if (namedFields.isNotEmpty() && positionalFields.isNotEmpty()) {
            out.emit(", ")
        }

        val joinedNamedFields = StringHelper.concatData(
            namedFields,
            prefix = CURLY_OPEN.toString(),
            postfix = CURLY_CLOSE.toString(),
            separator = COMMA_SEPARATOR,
        ) { "${it.second} ${it.first}" }
        out.emit(joinedNamedFields)

        out.emit(")")

        if (isNullable) {
            out.emit(NULLABLE_CHAR)
        }
        return out
    }

    override fun getRawData(): String = "Record"
}