package net.theevilreaper.dartpoet.parameter.minimized

import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.parameter.ParameterType
import net.theevilreaper.dartpoet.property.PropertySpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test provided functionality of minimized parameter")
class MinimizedParameterTest {

    @Test
    fun `test creation of a minimized parameter from a property spec`() {
        val propertySpec = PropertySpec.builder("name", String::class).build()
        val minimizedParameter = MinimizedParameter.fromProperty(propertySpec, ParameterType.REQUIRED)
        assertEquals("name", minimizedParameter.name)
        assertEquals(ParameterType.REQUIRED, minimizedParameter.type)
        assertTrue(minimizedParameter.self)
    }

    @Test
    fun `test creation of a minimized parameter from a property spec without self call`() {
        val propertySpec = PropertySpec.builder("name", String::class).build()
        val parameter = MinimizedParameter.fromProperty(propertySpec, ParameterType.REQUIRED, false)
        assertEquals("name", parameter.name)
        assertEquals(ParameterType.REQUIRED, parameter.type)
        assertFalse(parameter.self)
    }

    @Test
    fun `test creation of a minimized parameter from a property spec with initializer`() {
        val propertySpec = PropertySpec.builder("name", String::class).initWith("%C", "Test").build()
        val minimizedParameter = MinimizedParameter.fromProperty(propertySpec, ParameterType.REQUIRED)
        assertEquals("name", minimizedParameter.name)
        assertEquals(ParameterType.REQUIRED, minimizedParameter.type)
        assertNotNull(minimizedParameter.initializer)
        assertEquals(propertySpec.initBlock.build(), minimizedParameter.initializer)
        assertTrue(minimizedParameter.self)
    }

    @Test
    fun `test creation of a minimized parameter from a parameter spec`() {
        val parameterSpec = ParameterSpec.required("name", String::class).build()
        val minimizedParameter = MinimizedParameter.fromParameter(parameterSpec)
        assertEquals("name", minimizedParameter.name)
        assertEquals(ParameterType.REQUIRED, minimizedParameter.type)
        assertTrue(minimizedParameter.self)
    }

    @Test
    fun `test creation of a minimized parameter from a parameter spec without self call`() {
        val parameterSpec = ParameterSpec.positional("value", Integer::class).build()
        val minimizedParameter = MinimizedParameter.fromParameter(parameterSpec, false)
        assertEquals("value", minimizedParameter.name)
        assertEquals(ParameterType.POSITIONAL, minimizedParameter.type)
        assertFalse(minimizedParameter.self)
    }

    @Test
    fun `test creation of a minimized parameter from a parameter spec with initializer`() {
        val propertySpec = PropertySpec.builder("hasObject", Boolean::class).initWith("%L", "false").build()
        val minimizedParameter = MinimizedParameter.fromProperty(propertySpec, ParameterType.REQUIRED)
        assertEquals("hasObject", minimizedParameter.name)
        assertEquals(ParameterType.REQUIRED, minimizedParameter.type)
        assertNotNull(minimizedParameter.initializer)
        assertEquals(propertySpec.initBlock.build(), minimizedParameter.initializer)
        assertTrue(minimizedParameter.self)
    }
}
