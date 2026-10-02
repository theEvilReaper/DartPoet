package net.theevilreaper.dartpoet.verify

import com.google.common.truth.Truth.assertWithMessage
import net.theevilreaper.dartpoet.DartFile

/**
 * Checks the blank lines of generated Dart code against the rules which `dart format` applies.
 * Line wrapping and other formatting choices of `dart format` are not checked.
 *
 * - no blank line after an opening brace and no blank line before a closing brace
 * - never more than one blank line in a row
 * - a blank line after a top level block and around top level type declarations
 * - a file ends with exactly one line break
 *
 * @since 2.6.1
 * @author theEvilReaper
 */
internal object BlankLineStyle {

    private val TYPE_DECLARATION =
        Regex("""^(?:(?:abstract|base|final|interface|sealed|mixin)\s+)*(?:class|mixin|enum|extension)\b""")

    /**
     * Returns all blank line violations of the given [source].
     * @param source the generated Dart code
     * @param isFile whether the source is a whole file, which must end with exactly one line break
     * @return the violations, empty if the blank lines follow the Dart style
     */
    fun violations(source: String, isFile: Boolean): List<String> {
        val result = mutableListOf<String>()
        var lines = source.split('\n')
        if (isFile) {
            if (!source.endsWith("\n") || source.endsWith("\n\n")) {
                result += "The file must end with exactly one line break"
            }
            if (source.endsWith("\n")) lines = lines.dropLast(1)
        }

        lines.forEachIndexed { index, line ->
            val lineNumber = index + 1
            if (line.isBlank()) {
                if (index > 0 && lines[index - 1].trimEnd().endsWith("{")) {
                    result += "Line $lineNumber: blank line after an opening brace"
                }
                if (index + 1 < lines.size && lines[index + 1].trimStart().startsWith("}")) {
                    result += "Line $lineNumber: blank line before a closing brace"
                }
                if (index > 0 && lines[index - 1].isBlank()) {
                    result += "Line $lineNumber: more than one blank line"
                }
                return@forEachIndexed
            }

            // The remaining rules only apply to top level declarations
            if (index == 0 || line.first().isWhitespace() || line.startsWith("}")) return@forEachIndexed
            val previous = lines[index - 1]
            if (previous.isBlank()) return@forEachIndexed

            // Documentation and annotations belong to the declaration which follows them
            var start = index
            while (start > 0 && (lines[start - 1].startsWith("///") || lines[start - 1].startsWith("@"))) start--

            val closesBlock = previous == "}" ||
                (TYPE_DECLARATION.containsMatchIn(previous) && previous.trimEnd().endsWith("{}"))
            when {
                TYPE_DECLARATION.containsMatchIn(line) && start > 0 && lines[start - 1].isNotBlank() ->
                    result += "Line ${start + 1}: missing blank line before a type declaration"
                start == index && closesBlock ->
                    result += "Line $lineNumber: missing blank line after a block"
            }
        }
        return result
    }
}

/**
 * Fails if the blank lines of the given generated [source] don't follow the Dart style.
 * @param source the generated Dart code
 * @param isFile whether the source is a whole file
 */
internal fun assertBlankLineStyle(source: String, isFile: Boolean) {
    assertWithMessage("The blank lines don't follow the Dart style:\n$source")
        .that(BlankLineStyle.violations(source, isFile))
        .isEmpty()
}

/**
 * Fails if the blank lines of the generated code of this object don't follow the Dart style.
 * A [DartFile] is checked as a whole file.
 */
internal fun Any.assertBlankLineStyle() = assertBlankLineStyle(toString(), isFile = this is DartFile)
