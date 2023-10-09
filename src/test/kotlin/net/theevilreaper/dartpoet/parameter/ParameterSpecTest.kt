package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertContentEquals

class ParameterSpecTest {

    companion object {

        @JvmStatic
        private fun invalidParameterCreation() = Stream.of(
            Arguments.of(
                "The name of a parameter can't be empty",
                { ParameterSpec.builder("").build() }
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("invalidParameterCreation")
    fun `test invalid parameter creation`(exceptionMessage: String, parameterSpec: () -> ParameterSpec) {
        val exception = assertThrows<IllegalArgumentException> { parameterSpec() }
        assertEquals(exceptionMessage, exception.message)
    }

    @Test
    fun `test spec to builder conversation`() {
        val parameterSpec = ParameterSpec.builder("amount", Int::class)
            .nullable(true)
            .initializer("%L", "10")
            .annotation(AnnotationSpec.builder("nullable").build())
            .build()
        val specAsBuilder = parameterSpec.toBuilder()
        assertNotNull(parameterSpec)
        assertEquals(parameterSpec.name, specAsBuilder.name)
        assertEquals(parameterSpec.type, specAsBuilder.typeName)
        assertEquals(parameterSpec.isNullable, specAsBuilder.nullable)
        assertTrue { specAsBuilder.initializer!!.isNotEmpty() }
        assertContentEquals(parameterSpec.annotations, specAsBuilder.specData.annotations)
    }
}