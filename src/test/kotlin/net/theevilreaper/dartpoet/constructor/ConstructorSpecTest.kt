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
    fun `test modifier lambda overload adds modifier`() {
        val constructorSpec = ConstructorSpec.builder("TestModel")
            .modifier { DartModifier.CONST }
            .build()
        assertTrue(constructorSpec.modifiers.contains(DartModifier.CONST))
    }
}
