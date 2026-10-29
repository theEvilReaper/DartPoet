package net.theevilreaper.dartpoet.constructor

import net.theevilreaper.dartpoet.DartModifier
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test constructor spec creation")
class ConstructorSpecTest {

    @Test
    fun `test spec to builder conversation`() {
        val constructorSpec = ConstructorSpec.builder("TestModel")
            .build()
        val specAsBuilder = constructorSpec.toBuilder()
        assertEquals(constructorSpec.name, specAsBuilder.name)
    }

    @Test
    fun `test builder changes after build don't affect the spec`() {
        val builder = ConstructorSpec.builder("TestModel").addCode("a = %L", 1)
        val constructorSpec = builder.build()
        val expected = constructorSpec.toString()
        builder.addCode(", b = %L", 2)
        assertEquals(expected, constructorSpec.toString())
    }

    @Test
    fun `test spec to builder conversation keeps initializer`() {
        val constructorSpec = ConstructorSpec.builder("TestModel")
            .addCode("a = %L, b = %S", 1, "value")
            .build()
        val rebuiltSpec = constructorSpec.toBuilder().build()
        assertEquals(constructorSpec.toString(), rebuiltSpec.toString())
    }

    @Test
    fun `test modifier lambda overload adds modifier`() {
        val constructorSpec = ConstructorSpec.builder("TestModel")
            .modifier { DartModifier.CONST }
            .build()
        assertTrue(constructorSpec.modifiers.contains(DartModifier.CONST))
    }

    @Test
    fun `test lambda constructor throws exception`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            @Suppress("DEPRECATION")
            ConstructorSpec.builder("TestModel")
                .lambda(true)
                .addCode(net.theevilreaper.dartpoet.code.CodeBlock.of("field = 1"))
                .build()
        }
        assertEquals("A generative constructor can't use the arrow syntax. Use a factory constructor instead", exception.message)
    }

    @Test
    fun `test external constructor with initializer throws exception`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            ConstructorSpec.builder("TestModel")
                .modifier(DartModifier.EXTERNAL)
                .addCode(net.theevilreaper.dartpoet.code.CodeBlock.of("field = 1"))
                .build()
        }
        assertEquals("An external constructor can't have an initializer", exception.message)
    }

    @Test
    fun `test external const constructor throws exception`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            ConstructorSpec.builder("TestModel")
                .modifiers(DartModifier.EXTERNAL, DartModifier.CONST)
                .build()
        }
        assertEquals("An external constructor can't be const", exception.message)
    }
}
