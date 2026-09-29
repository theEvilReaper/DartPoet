package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.pattern.RelationalOperator.GREATER
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.STRING
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test cases for the cast, null-check and null-assert patterns")
class UnaryPatternTest {

    private val name = Pattern.variable("name")

    @Test
    fun `test cast pattern`() {
        assertEquals("var name as int", Pattern.cast(name, INTEGER).toString())
    }

    @Test
    fun `test null check pattern`() {
        assertEquals("var name?", Pattern.nullCheck(name).toString())
        assertEquals("String name?", Pattern.nullCheck(Pattern.variable("name", STRING)).toString())
    }

    @Test
    fun `test null assert pattern`() {
        assertEquals("var name!", Pattern.nullAssert(name).toString())
    }

    @Test
    fun `test unary patterns wrap non primary patterns in parentheses`() {
        val or = Pattern.or(Pattern.literal(1), Pattern.literal(2))
        assertEquals("(1 || 2)?", Pattern.nullCheck(or).toString())
        assertEquals("(> 0)!", Pattern.nullAssert(Pattern.relational(GREATER, 0)).toString())
        assertEquals("(var name as int)?", Pattern.nullCheck(Pattern.cast(name, INTEGER)).toString())
        assertEquals("(var name?) as String", Pattern.cast(Pattern.nullCheck(name), STRING).toString())
    }

    @Test
    fun `test unary patterns inside logical patterns need no parentheses`() {
        val pattern = Pattern.and(Pattern.nullCheck(name), Pattern.cast(Pattern.wildcard(), INTEGER))
        assertEquals("var name? && _ as int", pattern.toString())
    }
}
