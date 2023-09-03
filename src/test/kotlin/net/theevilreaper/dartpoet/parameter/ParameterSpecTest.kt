package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class ParameterSpecTest {

    @Test
    fun `test spec to builder conversation`() {
        val parameterSpec = ParameterSpec.builder("amount", "int")
            .nullable(true)
            .initializer("%L", "10")
            .annotation(AnnotationSpec.builder("nullable").build())
            .build()
        val specAsBuilder = parameterSpec.toBuilder()
        assertNotNull(parameterSpec)
        assertEquals(parameterSpec.name, specAsBuilder.name)
        assertEquals(parameterSpec.type, specAsBuilder.type)
        assertEquals(parameterSpec.isNullable, specAsBuilder.nullable)
        assertTrue { specAsBuilder.initializer!!.isNotEmpty() }
        assertContentEquals(parameterSpec.annotations, specAsBuilder.specData.annotations)
    }
}