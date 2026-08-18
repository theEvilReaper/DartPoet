package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.code.CodeWriter
import net.theevilreaper.dartpoet.type.record.RecordEntry
import org.jetbrains.annotations.ApiStatus

/**
 * The [RecordHelper] contains utility methods to write Dart 3 record types.
 *
 * @since 2.3.0
 * @author theEvilReaper
 */
@ApiStatus.Internal
internal object RecordHelper {

    /**
     * Writes the given record components to the [CodeWriter].
     *
     * @param positionalFields the list of positional fields in the record
     * @param namedFields the list of named fields in the record
     * @param isNullable whether the record type itself is nullable
     * @param out the [CodeWriter] to write to
     * @return the [CodeWriter] instance
     */
    fun writeRecord(
        positionalFields: List<RecordEntry>,
        namedFields: List<RecordEntry>,
        isNullable: Boolean,
        out: CodeWriter,
    ): CodeWriter {
        out.emit(ROUND_OPEN)

        val hasPositional = positionalFields.isNotEmpty()
        val hasNamed = namedFields.isNotEmpty()

        if (hasPositional) {
            positionalFields.forEachIndexed { index, field ->
                field.typeName.emit(out)
                if (!field.name.isNullOrBlank()) {
                    out.emitSpace()
                    out.emit(field.name)
                }
                if (index < positionalFields.size - 1) {
                    out.emit(COMMA_SEPARATOR)
                }
            }

            // Single positional field without named fields requires a trailing comma in Dart 3: (int,)
            if (positionalFields.size == 1 && !hasNamed) {
                out.emit(",")
            }
        }

        if (hasPositional && hasNamed) {
            out.emit(COMMA_SEPARATOR)
        }

        if (hasNamed) {
            out.emit(CURLY_OPEN.toString())
            namedFields.forEachIndexed { index, field ->
                field.typeName.emit(out)
                out.emitSpace()
                out.emit(field.name ?: EMPTY_STRING)
                if (index < namedFields.size - 1) {
                    out.emit(COMMA_SEPARATOR)
                }
            }
            out.emit(CURLY_CLOSE.toString())
        }

        out.emit(ROUND_CLOSE)

        if (isNullable) {
            out.emit(NULLABLE_CHAR)
        }

        return out
    }
}
