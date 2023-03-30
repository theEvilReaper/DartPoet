package net.theevilreaper.dartpoet.code

import net.theevilreaper.dartpoet.util.toImmutableList
import net.theevilreaper.dartpoet.writer.FragmentPart
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * codeblock.add("this.add(%$1T);", object);
 *
 */

class CodeFragmentBuilder(
    internal val formatParts: MutableList<String> = mutableListOf(), // 0
    internal val args: MutableList<Any?> = mutableListOf(), // 0
) {

    fun add(format: String, vararg args: Any?): CodeFragmentBuilder = apply {
        if (format.trim().isEmpty()) return@apply

        var hasRelative = false
        var hasIndexed = false

        var relativeParameterCount = 0
        val indexedParameterCount = IntArray(args.size)

        var counter = 0

        while (counter < format.length) {
            // Checks if the char is a placeholder
            if (format[counter].isSingleCharNoArgPlaceholder) {
                formatParts += format[counter].toString()
                counter++
                continue
            }

            if (format[counter] != '%') {
                var nextPlaceholder = format.nextPotentialPlaceholderPosition(startIndex = counter + 1)
                if (nextPlaceholder == -1) nextPlaceholder = format.length
                formatParts += format.substring(counter, nextPlaceholder)
                counter = nextPlaceholder
                continue
            }

            counter++

            // Consume zero or more digits, leaving 'c' as the first non-digit char after the '%'.
            val indexStart = counter
            var c: Char
            do {
                require(counter < format.length) { "dangling format characters in '$format'" }
                c = format[counter++]
            } while (c in '0'..'9')
            val indexEnd = counter - 1

            // If 'c' doesn't take an argument, we're done.
            if (c.isMultiCharNoArgPlaceholder) {
                require(indexStart == indexEnd) { "%% may not have an index" }
                formatParts += "%$c"
                continue
            }

            val index: Int
            if (indexStart < indexEnd) {
                index = Integer.parseInt(format.substring(indexStart, indexEnd)) - 1
                hasIndexed = true
                if (args.isNotEmpty()) {
                    indexedParameterCount[index % args.size]++ // modulo is needed, checked below anyway
                }
            } else {
                index = relativeParameterCount
                hasRelative = true
                relativeParameterCount++
            }

            require(index >= 0 && index < args.size) {
                "index ${index + 1} for '${format.substring(
                    indexStart - 1,
                    indexEnd + 1,
                )}' not in range (received ${args.size} arguments)"
            }
            require(!hasIndexed || !hasRelative) { "cannot mix indexed and positional parameters" }

            addArgument(format, c, args[index])

            formatParts += "%$c"

        }

        if (hasRelative) {
            require(relativeParameterCount >= args.size) {
                "unused arguments: expected $relativeParameterCount, received ${args.size}"
            }
        }
        if (hasIndexed) {
            val unused = mutableListOf<String>()
            for (i in args.indices) {
                if (indexedParameterCount[i] == 0) {
                    unused += "%" + (i + 1)
                }
            }
            val s = if (unused.size == 1) "" else "s"
            require(unused.isEmpty()) { "unused argument$s: ${unused.joinToString(", ")}" }
        }
    }

    private fun addArgument(format: String, c: Char, arg: Any?) {
        val part: FragmentPart = FragmentPart.mapByIdentifier(c)
            ?: throw IllegalArgumentException(String.format("invalid format string: '%s'", format))

        when (part) {
            FragmentPart.NAMED -> this.args += argToName(arg)
            FragmentPart.LITERAL -> this.args += argToLiteral(arg)
            FragmentPart.STRING -> this.args += argToString(arg)
            FragmentPart.STRING_NOT_ESCAPED -> this.args += if (arg is CodeFragment) arg else argToString(arg)
            FragmentPart.MEMBER -> this.args += arg
        }
    }

    private fun argToName(o: Any?) = when (o) {
        is CharSequence -> o.toString()
       /* is ParameterSpec -> o.name
        is PropertySpec -> o.name
        is FunSpec -> o.name
        is TypeSpec -> o.name!!
        is MemberName -> o.simpleName*/
        else -> throw IllegalArgumentException("expected name but was $o")
    }

    private fun argToLiteral(o: Any?) = if (o is Number) formatNumericValue(o) else o

    private fun argToString(o: Any?) = o?.toString()

    private fun formatNumericValue(o: Number): Any? {
        val format = DecimalFormatSymbols().apply {
            decimalSeparator = '.'
            groupingSeparator = '_'
        }

        val precision = if (o is Float || o is Double) o.toString().split(".").last().length else 0

        val pattern = when (o) {
            is Float, is Double -> "###,##0.0" + "#".repeat(precision - 1)
            else -> "###,##0"
        }

        return DecimalFormat(pattern, format).format(o)
    }

    /*private fun argToType(o: Any?) = when (o) {
        *//*is TypeName -> o
        is TypeMirror -> {
            logDeprecationWarning(o)
            o.asTypeName()
        }
        is Element -> {
            logDeprecationWarning(o)
            o.asType().asTypeName()
        }
        is Type -> o.asTypeName()
        is KClass<*> -> o.asTypeName()*//*
        else -> throw IllegalArgumentException("expected type but was $o")
    }*/

    fun indent(): CodeFragmentBuilder = apply {
        formatParts += "⇥"
    }

    fun unindent(): CodeFragmentBuilder = apply {
        formatParts += "⇤"
    }

    fun clear(): CodeFragmentBuilder = apply {
        formatParts.clear()
        args.clear()
    }

    fun build(): CodeFragment = CodeFragment(formatParts.toImmutableList(), args.toImmutableList())
}
