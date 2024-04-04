package net.theevilreaper.dartpoet.parameter

import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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
                { ParameterSpec.builder("amount", Int::class).modifiers(DartModifier.REQUIRED) },
            ),
            Arguments.of(
                "String name = 'theEvilReaper'",
                { ParameterSpec.builder("name", String::class).initializer("%C", "theEvilReaper") },
            ),
            Arguments.of(
                "String name = 'theEvilReaper'",
                {
                    ParameterSpec.builder("name", String::class).named(true).initializer("%C", "theEvilReaper")
                },
            )
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