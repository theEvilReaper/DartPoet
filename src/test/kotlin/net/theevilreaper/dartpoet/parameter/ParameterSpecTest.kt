package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertContentEquals

@DisplayName("Test the definition of different ParameterSpec objects")
class ParameterSpecTest {

    companion object {

        @JvmStatic
        private fun parameterVariants(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "required int amount",
                { ParameterSpec.required("amount", Int::class) },
            ),
            Arguments.of(
                "String name = 'theEvilReaper'",
                { ParameterSpec.positional("name", String::class).initializer("%C", "theEvilReaper") },
            ),
            Arguments.of(
                "String name = 'theEvilReaper'",
                {
                    ParameterSpec.named("name", String::class).initializer("%C", "theEvilReaper")
                },
            ),
            Arguments.of(
                "super.name",
                { ParameterSpec.positional("name").superParameter() },
            ),
            Arguments.of(
                "required super.name",
                { ParameterSpec.required("name").superParameter() },
            ),
        )
    }

    @ParameterizedTest(name = "Test parameter generation for case: {arguments}")
    @MethodSource("parameterVariants")
    fun `test parameter spec builder`(expected: String, specBuilder: () -> ParameterBuilder) {
        val parameterSpec = specBuilder.invoke().build()
        assertNotNull(parameterSpec)
        assertEquals(expected, parameterSpec.toString())
    }

    @Test
    fun `test spec to builder conversation`() {
        val parameterSpec = ParameterSpec.positional("amount", Int::class)
            .nullable(true)
            .initializer("%L", "10")
            .annotation(AnnotationSpec.builder("nullable").build())
            .build()
        val specAsBuilder = parameterSpec.toBuilder()
        assertNotNull(parameterSpec)
        assertEquals(parameterSpec.name, specAsBuilder.name)
        assertEquals(parameterSpec.typeName, specAsBuilder.typeName)
        assertEquals(parameterSpec.isNullable, specAsBuilder.nullable)
        assertTrue { specAsBuilder.initializer!!.isNotEmpty() }
        assertContentEquals(parameterSpec.annotations, specAsBuilder.annotationData.annotations)
    }

    @Test
    fun `test super parameter with explicit typeName throws exception`() {
        val exception = assertThrows<IllegalStateException> {
            ParameterSpec.positional("name", String::class).superParameter().build()
        }
        assertTrue(exception.message?.contains("super parameter") == true)
    }

    @Test
    fun `test super parameter spec to builder conversion`() {
        val parameterSpec = ParameterSpec.positional("name").superParameter().build()
        val specAsBuilder = parameterSpec.toBuilder()
        assertTrue(specAsBuilder.isSuperParameter)
    }

    @Test
    fun `test coVariant spec to builder conversion`() {
        val parameterSpec = ParameterSpec.positional("name", String::class)
            .coVariant(true)
            .build()
        val specAsBuilder = parameterSpec.toBuilder()
        assertTrue(specAsBuilder.coVariant)
    }
}
