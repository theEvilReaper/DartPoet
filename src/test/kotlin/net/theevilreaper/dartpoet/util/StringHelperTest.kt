package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.TypeName
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test each method from the StringHelper utility class")
class StringHelperTest {

    @Test
    fun `test modifier concatenation`() {
        val modifiers = setOf(DartModifier.CONST, DartModifier.FINAL, DartModifier.FACTORY)
        val result = StringHelper.concatData(modifiers, separator = SPACE) { it.identifier }
        assertEquals("const final factory", result)
        assertEquals(EMPTY_STRING, StringHelper.concatData<DartModifier>(emptySet()) { it.identifier })
    }

    @Test
    fun `test generic concatenation`() {
        val generics = listOf(ClassName("A"), ClassName("B"), ClassName("C"))
        val result = StringHelper.concatData<TypeName>(
            generics,
            prefix = "<",
            separator = ", ",
            postfix = ">"
        ) { it.getRawData() }
        assertEquals("<A, B, C>", result)
    }

    @Test
    fun `test ensure variable name with private modifier`() {
        val name = "test"
        val result = StringHelper.ensureVariableNameWithPrivateModifier(name, true)
        assertEquals("_$name", result)
        assertEquals(name, StringHelper.ensureVariableNameWithPrivateModifier(name, false))
    }

}