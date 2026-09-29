package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.pattern.RelationalOperator.GREATER
import net.theevilreaper.dartpoet.pattern.RelationalOperator.LESS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test cases for the LogicalPattern implementation")
class LogicalPatternTest {

    private val one = Pattern.literal(1)
    private val two = Pattern.literal(2)
    private val three = Pattern.literal(3)

    @Test
    fun `test logical or pattern`() {
        assertEquals("1 || 2 || 3", Pattern.or(one, two, three).toString())
    }

    @Test
    fun `test logical and pattern`() {
        val pattern = Pattern.and(Pattern.relational(GREATER, 0), Pattern.relational(LESS, 10))
        assertEquals("> 0 && < 10", pattern.toString())
    }

    @Test
    fun `test or inside and gets parentheses`() {
        assertEquals("(1 || 2) && 3", Pattern.and(Pattern.or(one, two), three).toString())
    }

    @Test
    fun `test and inside or needs no parentheses`() {
        assertEquals("1 && 2 || 3", Pattern.or(Pattern.and(one, two), three).toString())
    }

    @Test
    fun `test nested patterns of the same operator need no parentheses`() {
        assertEquals("1 || 2 || 3", Pattern.or(Pattern.or(one, two), three).toString())
        assertEquals("1 && 2 && 3", Pattern.and(one, Pattern.and(two, three)).toString())
    }

    @Test
    fun `test logical pattern inside a record needs no parentheses`() {
        assertEquals("(1 || 2, 3)", Pattern.record(Pattern.or(one, two), three).toString())
    }

    @Test
    fun `test logical pattern needs at least two patterns`() {
        val exception = assertThrows(IllegalArgumentException::class.java) { Pattern.or(one) }
        assertEquals("A logical pattern needs at least two patterns", exception.message)
    }
}
