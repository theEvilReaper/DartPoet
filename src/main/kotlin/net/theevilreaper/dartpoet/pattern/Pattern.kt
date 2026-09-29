package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.type.asClassName
import net.theevilreaper.dartpoet.type.asTypeName
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * A [Pattern] represents a Dart 3 pattern, as used inside a `switch` case or switch expression case.
 * Patterns are not validated against the matched value's shape - they only describe how the pattern
 * itself renders as Dart source.
 *
 * @author theEvilReaper
 * @since 2.5.0
 */
sealed class Pattern {

    /**
     * The precedence of the pattern, which decides if it needs parentheses when it's nested in another pattern.
     * Most patterns are primary patterns and never need parentheses.
     */
    internal open val precedence: Int get() = PRIMARY_PRECEDENCE

    /**
     * Renders the pattern and wraps it in parentheses if its precedence is lower than [minimum].
     * @param minimum the lowest precedence which can be used without parentheses
     * @return the rendered pattern
     */
    internal fun render(minimum: Int): String = if (precedence < minimum) "($this)" else toString()

    /**
     * The patterns which are nested directly inside this pattern.
     */
    internal open val subPatterns: List<Pattern> get() = emptyList()

    companion object {

        // Precedence levels from the Dart grammar, from the loosest to the tightest binding
        internal const val LOGICAL_OR_PRECEDENCE = 1
        internal const val LOGICAL_AND_PRECEDENCE = 2
        internal const val RELATIONAL_PRECEDENCE = 3
        internal const val UNARY_PRECEDENCE = 4
        internal const val PRIMARY_PRECEDENCE = 5

        /**
         * Creates an untyped wildcard pattern (`_`).
         * @return the created [Pattern]
         */
        @JvmStatic
        fun wildcard(): Pattern = WildcardPattern()

        /**
         * Creates a typed wildcard pattern (e.g. `int _`).
         * @param typeName the required type of the matched value
         * @return the created [Pattern]
         */
        @JvmStatic
        fun wildcard(typeName: TypeName): Pattern = WildcardPattern(typeName)

        /**
         * Creates a typed wildcard pattern from a [Type].
         * @param type the required type of the matched value
         * @return the created [Pattern]
         */
        @JvmStatic
        fun wildcard(type: Type): Pattern = WildcardPattern(type.asTypeName())

        /**
         * Creates a typed wildcard pattern from a [KClass].
         * @param type the required type of the matched value
         * @return the created [Pattern]
         */
        @JvmStatic
        fun wildcard(type: KClass<*>): Pattern = WildcardPattern(type.asClassName())

        /**
         * Creates a literal pattern matching a constant [value] (number, string, boolean or `null`).
         * @param value the constant value to match
         * @return the created [Pattern]
         */
        @JvmStatic
        fun literal(value: Any?): Pattern = LiteralPattern(value)

        /**
         * Creates a variable pattern that binds the matched value to [name] (`var name`).
         * @param name the name of the bound variable
         * @param isFinal true to declare the binding `final` instead of `var`
         * @return the created [Pattern]
         */
        @JvmOverloads
        @JvmStatic
        fun variable(name: String, isFinal: Boolean = false): Pattern = VariablePattern(name, isFinal = isFinal)

        /**
         * Creates a typed variable pattern that binds the matched value to [name], constrained to [typeName].
         * @param name the name of the bound variable
         * @param typeName the required type of the matched value
         * @param isFinal true to declare the binding `final`
         * @return the created [Pattern]
         */
        @JvmOverloads
        @JvmStatic
        fun variable(name: String, typeName: TypeName, isFinal: Boolean = false): Pattern =
            VariablePattern(name, typeName, isFinal)

        /**
         * Creates a typed variable pattern from a [KClass].
         * @param name the name of the bound variable
         * @param type the required type of the matched value
         * @param isFinal true to declare the binding `final`
         * @return the created [Pattern]
         */
        @JvmOverloads
        @JvmStatic
        fun variable(name: String, type: KClass<*>, isFinal: Boolean = false): Pattern =
            VariablePattern(name, type.asClassName(), isFinal)

        /**
         * Creates a record pattern that destructures the given [positional] fields, e.g. `(var x, var y)`.
         * @param positional the positional sub-patterns, in order
         * @return the created [Pattern]
         */
        @JvmStatic
        fun record(vararg positional: Pattern): Pattern = RecordPattern(positional.toList(), emptyMap())

        /**
         * Creates a record pattern that destructures the given [positional] and [named] fields, e.g.
         * `(0, name: var name)`.
         * @param positional the positional sub-patterns, in order
         * @param named the named sub-patterns, keyed by field name
         * @return the created [Pattern]
         */
        @JvmStatic
        fun record(positional: List<Pattern>, named: Map<String, Pattern>): Pattern =
            RecordPattern(positional, named)

        /**
         * Creates a list pattern that destructures the given [elements] by position, with no rest element.
         * @param elements the sub-patterns, in order
         * @return the created [Pattern]
         */
        @JvmStatic
        fun list(vararg elements: Pattern): Pattern = ListPattern(elements.toList())

        /**
         * Creates a list pattern with a rest element (`...` or `...rest`) between [before] and [after].
         * @param before the sub-patterns before the rest element
         * @param rest the identifier the rest element binds to, or `null` to ignore it (`...`)
         * @param after the sub-patterns after the rest element
         * @return the created [Pattern]
         */
        @JvmOverloads
        @JvmStatic
        fun listWithRest(before: List<Pattern>, rest: String? = null, after: List<Pattern> = emptyList()): Pattern =
            ListPattern(before, hasRest = true, restName = rest, after = after)

        /**
         * Creates an object pattern that destructures [type] via the given [fields], e.g.
         * `Point(x: var px, y: var py)`.
         * @param type the type being destructured
         * @param fields the sub-patterns, keyed by getter name
         * @return the created [Pattern]
         */
        @JvmStatic
        fun objectPattern(type: TypeName, fields: Map<String, Pattern>): Pattern = ObjectPattern(type, fields)

        /**
         * Creates an object pattern from a [KClass].
         * @param type the type being destructured
         * @param fields the sub-patterns, keyed by getter name
         * @return the created [Pattern]
         */
        @JvmStatic
        fun objectPattern(type: KClass<*>, fields: Map<String, Pattern>): Pattern =
            ObjectPattern(type.asClassName(), fields)

        /**
         * Creates a map pattern that matches a map containing the given [entries], e.g. `{'id': int id}`.
         * The keys must be constant values: strings, numbers, booleans, `null` or a [CodeBlock]
         * which is emitted verbatim (e.g. `Color.red`).
         * @param entries the sub-patterns, keyed by map key
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun map(entries: Map<*, Pattern>): Pattern = MapPattern(entries)

        /**
         * Creates a relational pattern that compares the matched value with [operand], e.g. `>= 0`.
         * The operand must be a constant value: a string, number, boolean, `null` or a [CodeBlock]
         * which is emitted verbatim.
         * @param operator the comparison operator
         * @param operand the constant to compare with
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun relational(operator: RelationalOperator, operand: Any?): Pattern = RelationalPattern(operator, operand)

        /**
         * Creates a logical-or pattern which matches if any of the given [patterns] matches, e.g. `1 || 2`.
         * @param patterns the alternatives, at least two
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun or(vararg patterns: Pattern): Pattern = LogicalPattern(LogicalPattern.Operator.OR, patterns.toList())

        /**
         * Creates a logical-and pattern which matches if all given [patterns] match, e.g. `> 0 && < 10`.
         * @param patterns the patterns which must match, at least two
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun and(vararg patterns: Pattern): Pattern = LogicalPattern(LogicalPattern.Operator.AND, patterns.toList())

        /**
         * Creates a cast pattern which casts the matched value to [type] before matching [pattern],
         * e.g. `var x as int`. The cast throws at runtime if the value has a different type.
         * @param pattern the pattern to match after the cast
         * @param type the type to cast to
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun cast(pattern: Pattern, type: TypeName): Pattern = CastPattern(pattern, type)

        /**
         * Creates a null-check pattern which only matches a non-null value, e.g. `var name?`.
         * @param pattern the pattern to match against the non-null value
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun nullCheck(pattern: Pattern): Pattern = NullCheckPattern(pattern)

        /**
         * Creates a null-assert pattern which throws if the matched value is null, e.g. `var name!`.
         * @param pattern the pattern to match against the non-null value
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun nullAssert(pattern: Pattern): Pattern = NullAssertPattern(pattern)

        /**
         * Creates an identifier pattern which is only the given [name], e.g. `a`.
         * It's meant for pattern declarations, pattern assignments and for-in loops, where it binds or
         * assigns the variable. Inside a `case` or an if-case, Dart treats a plain identifier as a
         * constant to compare with, so use [variable] to bind a value there.
         * @param name the name of the variable
         * @return the created [Pattern]
         * @since 2.6.0
         */
        @JvmStatic
        fun identifier(name: String): Pattern = IdentifierPattern(name)

        /**
         * Creates a pattern that emits [expression] verbatim - an escape hatch for pattern syntax
         * not modeled as a dedicated [Pattern]. A raw pattern is never wrapped in parentheses.
         * @param expression the raw Dart pattern expression
         * @return the created [Pattern]
         */
        @JvmStatic
        fun raw(expression: String): Pattern = RawPattern(expression)
    }
}

/**
 * Renders a constant value, as used for map pattern keys and relational pattern operands.
 * A [CodeBlock] is emitted verbatim, all other values are rendered like a [LiteralPattern].
 * @param value the constant value
 * @return the rendered constant
 */
internal fun patternConstant(value: Any?): String = when (value) {
    is CodeBlock -> value.toString()
    else -> LiteralPattern(value).toString()
}

/**
 * Checks that this pattern can be used as the outer pattern of a pattern declaration, a for-in loop or
 * a pattern assignment. Raw patterns are not checked.
 * @param context the name of the statement, used in the error messages
 * @param isAssignment whether the pattern assigns existing variables instead of declaring new ones
 */
internal fun Pattern.requireOuterPattern(context: String, isAssignment: Boolean = false) {
    if (this is RawPattern) return
    require(this is RecordPattern || this is ListPattern || this is MapPattern || this is ObjectPattern) {
        "A $context needs a record, list, map or object pattern"
    }
    nestedPatterns().filterIsInstance<VariablePattern>().forEach { variable ->
        require(!isAssignment) {
            "A $context can't declare the variable '${variable.name}', use Pattern.identifier instead"
        }
        require(variable.typeName != null && !variable.isFinal) {
            "Variable patterns in a $context can't use var or final, use Pattern.identifier or a typed variable pattern instead"
        }
    }
}

private fun Pattern.nestedPatterns(): Sequence<Pattern> = sequence {
    yield(this@nestedPatterns)
    subPatterns.forEach { yieldAll(it.nestedPatterns()) }
}
