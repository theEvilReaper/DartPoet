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

    if ('\n' in value) {
        return "$tripleQuote${value.replace(quote, "\\$quote")}$tripleQuote"
    }

    val escaped = buildString {
        for (c in value) {
            when (c) {
                quoteChar -> append("\\$quoteChar")
                '\\' -> append("\\\\")
                '$' -> if (escapeDollar) append("\\$") else append(c)
                '\t' -> append("\\t")
                '\r' -> append("\\r")
                else -> append(c)
            }
        }
    }
    return "$quote$escaped$quote"
}

private val DART_KEYWORDS = setOf(
    "abstract", "as", "assert", "async", "await", "base", "break", "case", "catch", "class",
    "const", "continue", "covariant", "default", "deferred", "do", "dynamic", "else", "enum",
    "export", "extends", "extension", "external", "factory", "false", "final", "finally", "for",
    "Function", "get", "hide", "if", "implements", "import", "in", "interface", "is", "late",
    "library", "mixin", "new", "null", "on", "operator", "part", "required", "rethrow", "return",
    "sealed", "set", "show", "static", "super", "switch", "sync", "this", "throw", "true", "try",
    "typedef", "var", "void", "when", "while", "with", "yield"
)

internal val String.isKeyword get() = this in DART_KEYWORDS

internal fun String.escapeIfNecessary(validate: Boolean = true): String {
    if (validate) {
        require(!isKeyword) { "The given name '$this' is a Dart keyword and cannot be used as an identifier." }
        require(isValidDartIdentifier()) { "The given name '$this' is not a valid Dart identifier." }
    }
    return this
}

private fun String.isValidDartIdentifier(): Boolean {
    if (isEmpty()) return false
    val first = first()
    if (!first.isLetter() && first != '_' && first != '$') return false
    return drop(1).all { it.isLetterOrDigit() || it == '_' || it == '$' }
}
