package net.theevilreaper.dartpoet.function.constructor

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConstructorSpecTest {
    @Test
    fun `test spec to builder conversation`() {
        val constructorSpec = ConstructorSpec.builder("TestModel")
            .asFactory(true)
            .build()
        val specAsBuilder = constructorSpec.toBuilder()
        assertEquals(constructorSpec.name, specAsBuilder.name)
        assertEquals(constructorSpec.isFactory, specAsBuilder.factory)
    }
}