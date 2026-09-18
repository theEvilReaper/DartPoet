package net.theevilreaper.dartpoet.pattern

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

    companion object {

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
         * Creates a pattern that emits [expression] verbatim - an escape hatch for pattern syntax
         * not yet modeled as a dedicated [Pattern] (e.g. relational `< 0`, logical `a || b`,
         * null-check `x?`, null-assert `x!`, or cast `x as int` patterns).
         * @param expression the raw Dart pattern expression
         * @return the created [Pattern]
         */
        @JvmStatic
        fun raw(expression: String): Pattern = RawPattern(expression)
    }
}
