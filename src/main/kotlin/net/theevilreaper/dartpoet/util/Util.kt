/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Changes to this class:
 *  - it contains only the methods which are needed
 */
package net.theevilreaper.dartpoet.util

import java.util.Collections

internal fun <T> Collection<T>.toImmutableList(): List<T> =
    Collections.unmodifiableList(ArrayList(this))

internal fun <T> Collection<T>.toImmutableSet(): Set<T> =
    Collections.unmodifiableSet(LinkedHashSet(this))

internal fun <T> T.isOneOf(t1: T, t2: T, t3: T? = null, t4: T? = null, t5: T? = null, t6: T? = null) =
    this == t1 || this == t2 || this == t3 || this == t4 || this == t5 || this == t6

// see https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6
internal fun characterLiteralWithoutSingleQuotes(c: Char) = when {
    c == '\b' -> "\\b" // \u0008: backspace (BS)
    c == '\t' -> "\\t" // \u0009: horizontal tab (HT)
    c == '\n' -> "\\n" // \u000a: linefeed (LF)
    c == '\r' -> "\\r" // \u000d: carriage return (CR)
    c == '\"' -> "\"" // \u0022: double quote (")
    c == '\'' -> "\\'" // \u0027: single quote (')
    c == '\\' -> "\\\\" // \u005c: backslash (\)
    c.isIsoControl -> String.format("\\u%04x", c.code)
    else -> c.toString()
}

internal fun escapeCharacterLiterals(s: String) = buildString {
    for (c in s) append(characterLiteralWithoutSingleQuotes(c))
}

private val Char.isIsoControl: Boolean
    get() {
        return this in '\u0000'..'\u001F' || this in '\u007F'..'\u009F'
    }

internal fun dartStringLiteral(
    value: String,
    quoteChar: Char = '\'',
    escapeDollar: Boolean = true,
): String {
    val quote = quoteChar.toString()
    val tripleQuote = quote.repeat(3)

    val multiline = '\n' in value
    // Dart drops a whitespace-only first line of a multiline literal, so its line break is escaped
    var escapeFirstNewline = multiline && value.substringBefore('\n').all { it == ' ' || it == '\t' }

    val escaped = buildString {
        var index = 0
        while (index < value.length) {
            // Interpolated expressions are Dart code, so they are copied without escaping
            if (!escapeDollar && value.startsWith("\${", index)) {
                val end = value.interpolationEnd(index + 2)
                if (end != -1) {
                    append(value, index, end + 1)
                    index = end + 1
                    continue
                }
            }
            val c = value[index++]
            when {
                c == quoteChar -> append("\\$quoteChar")
                c == '\\' -> append("\\\\")
                c == '$' -> if (escapeDollar) append("\\$") else append(c)
                // A raw carriage return is normalized to a newline by Dart, even in multiline literals
                c == '\r' -> append("\\r")
                c == '\n' && escapeFirstNewline -> {
                    append("\\n")
                    escapeFirstNewline = false
                }
                // Multiline literals keep their tabs and line breaks as written
                multiline -> append(c)
                c == '\t' -> append("\\t")
                else -> append(c)
            }
        }
    }
    val delimiter = if (multiline) tripleQuote else quote
    return "$delimiter$escaped$delimiter"
}

/**
 * Returns the index of the `}` which closes an interpolation whose expression starts at [start],
 * or -1 if the interpolation is not closed.
 * Braces are counted, so an unbalanced brace inside a nested string literal (e.g. `${f('}')}`) is not supported.
 */
private fun String.interpolationEnd(start: Int): Int {
    var depth = 1
    for (i in start until length) {
        when (this[i]) {
            '{' -> depth++
            '}' -> if (--depth == 0) return i
        }
    }
    return -1
}

/**
 * Reserved words which can never be used as an identifier.
 * Contextual keywords (`await`, `yield`) and unrestricted keywords (e.g. `on`, `when`) are valid identifiers.
 * @see <a href="https://dart.dev/language/keywords">Dart keywords</a>
 */
private val DART_RESERVED_WORDS = setOf(
    "assert", "break", "case", "catch", "class", "const", "continue", "default", "do", "else",
    "enum", "extends", "false", "final", "finally", "for", "if", "in", "is", "new", "null",
    "rethrow", "return", "super", "switch", "this", "throw", "true", "try", "var", "void",
    "while", "with"
)

/**
 * Built-in identifiers which can't be used as the name of a type, an extension or an import prefix,
 * but are valid identifiers in all other places.
 * @see <a href="https://dart.dev/language/keywords">Dart keywords</a>
 */
private val DART_BUILT_IN_IDENTIFIERS = setOf(
    "abstract", "as", "covariant", "deferred", "dynamic", "export", "extension", "external",
    "factory", "Function", "get", "implements", "import", "interface", "late", "library", "mixin",
    "operator", "part", "required", "set", "static", "type", "typedef"
)

internal val String.isReservedWord get() = this in DART_RESERVED_WORDS

/**
 * Validates that this string can be used as a Dart identifier.
 * @param validate whether the validation should be performed
 * @param isTypeName whether the identifier names a type, which additionally forbids built-in identifiers
 */
internal fun String.escapeIfNecessary(validate: Boolean = true, isTypeName: Boolean = false): String {
    if (validate) {
        require(!isReservedWord) { "The given name '$this' is a reserved word and cannot be used as an identifier." }
        require(!isTypeName || this !in DART_BUILT_IN_IDENTIFIERS) {
            "The given name '$this' is a built-in identifier and can't be used as a type name."
        }
        require(isValidDartIdentifier()) { "The given name '$this' is not a valid Dart identifier." }
    }
    return this
}

/**
 * Checks if this string matches the Dart identifier grammar, which only allows ASCII letters and digits.
 */
private fun String.isValidDartIdentifier(): Boolean {
    if (isEmpty()) return false
    if (!first().isIdentifierStart()) return false
    return drop(1).all { it.isIdentifierStart() || it in '0'..'9' }
}

private fun Char.isIdentifierStart(): Boolean = this in 'a'..'z' || this in 'A'..'Z' || this == '_' || this == '$'
