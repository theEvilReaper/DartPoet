package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.DartModifier
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test each method from the StringHelper utility class")
class StringHelperTest {

    @Test
    fun `test modifier concatenation`() {
        val modifiers = setOf(DartModifier.CONST, DartModifier.FINAL, DartModifier.FACTORY)
        val result = StringHelper.joinModifiers(modifiers, separator = SPACE)
        assertEquals("const final factory", result)
        assertEquals(EMPTY_STRING, StringHelper.joinModifiers(emptySet()))
    }

    @Test
    fun `test ensure variable name with private modifier`() {
        val name = "test"
        val result = StringHelper.ensureVariableNameWithPrivateModifier(name,true)
        assertEquals("_$name", result)
        assertEquals(name, StringHelper.ensureVariableNameWithPrivateModifier(name, false))
    }
}