package net.theevilreaper.dartpoet.enumeration.parameter

import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EnumParameterSpecTest {

    @Test
    fun `test enum parameter creation with empty code block`() {
        assertThrows<IllegalStateException>("The data block can't be empty") {
            EnumParameterSpec.from(EMPTY_STRING, EMPTY_STRING, false)
        }
    }

    @Test
    fun `test enum parameter creation with more than one argument`() {
        assertThrows<IllegalArgumentException>("The data block can't have more than one argument") {
            EnumParameterSpec.required("%L %L", "test", "test", variableRef = "test")
        }
    }

    @Test
    fun `test enum parameter creation with missing variable reference`() {
        assertThrows<IllegalArgumentException>("The variable reference can't be null when the parameter is required") {
            EnumParameterSpec.required("%L", "test", variableRef = EMPTY_STRING)
        }
    }

    @Test
    fun `test enum parameter creation`() {
        val parameter = EnumParameterSpec.from("%C", "test")
        assertNotNull(parameter)
        assertNotNull(parameter.dataBlock)
        assertEquals(1, parameter.dataBlock.args.size)
        assertEquals("test", parameter.dataBlock.args[0])
        assertEquals(1, parameter.dataBlock.formatParts.size)
        assertEquals("%C", parameter.dataBlock.formatParts[0])
        assertFalse(parameter.required)
        assertNull(parameter.variableRef)
    }
}