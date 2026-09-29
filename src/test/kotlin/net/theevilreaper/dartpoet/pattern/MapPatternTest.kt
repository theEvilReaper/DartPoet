package net.theevilreaper.dartpoet.pattern

import net.theevilreaper.dartpoet.code.CodeBlock
import net.theevilreaper.dartpoet.type.INTEGER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test cases for the MapPattern implementation")
class MapPatternTest {

    @Test
    fun `test map pattern with string keys`() {
        val pattern = Pattern.map(linkedMapOf("id" to Pattern.variable("id", INTEGER), "name" to Pattern.wildcard()))
        assertEquals("{'id': int id, 'name': _}", pattern.toString())
    }

    @Test
    fun `test map pattern with constant keys`() {
        val pattern = Pattern.map(
            linkedMapOf(1 to Pattern.literal("one"), true to Pattern.wildcard(), CodeBlock.of("Color.red") to Pattern.variable("c"))
        )
        assertEquals("{1: 'one', true: _, Color.red: var c}", pattern.toString())
    }

    @Test
    fun `test map pattern keeps nested logical patterns without parentheses`() {
        val pattern = Pattern.map(mapOf("code" to Pattern.or(Pattern.literal(200), Pattern.literal(201))))
        assertEquals("{'code': 200 || 201}", pattern.toString())
    }

    @Test
    fun `test empty map pattern is rejected`() {
        val exception = assertThrows(IllegalArgumentException::class.java) { Pattern.map(emptyMap<String, Pattern>()) }
        assertEquals("A map pattern must have at least one entry", exception.message)
    }
}
