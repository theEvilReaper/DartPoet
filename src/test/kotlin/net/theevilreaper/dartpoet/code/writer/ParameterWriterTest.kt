package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ParameterWriterTest {

    companion object {

        @JvmStatic
        private fun parameterTests(): Stream<Arguments> = Stream.of(
            Arguments.of(ParameterSpec.builder("age", Int::class).build(), "int age"),
            Arguments.of(ParameterSpec.builder("age", Int::class).initializer("%L", "10").build(), "int age = 10"),
            Arguments.of(ParameterSpec.builder("test", String::class).nullable(true).build(), "String? test"),
            Arguments.of(
                ParameterSpec.builder("list", List::class.parameterizedBy(Int::class)).build(),
                "List<int> list"
            ),
            Arguments.of(
                ParameterSpec.builder("list", List::class.parameterizedBy(Int::class)).initializer("%L", "[]").build(),
                "List<int> list = []"
            ),
            Arguments.of(
                ParameterSpec.builder("map", Map::class.parameterizedBy(String::class, Int::class)).build(),
                "Map<String, int> map"
            ),
            Arguments.of(
                ParameterSpec.builder("map", Map::class.parameterizedBy(String::class, Int::class))
                    .initializer("%L", "{}").build(),
                "Map<String, int> map = {}"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("parameterTests")
    fun `test parameter write`(parameterSpec: ParameterSpec, expected: String) {
        assertThat(parameterSpec.toString()).isEqualTo(expected)
    }

    @Test
    fun `test invalid parameter definition`() {
        assertThrows(
            IllegalStateException::class.java,
            { ParameterSpec.builder("").build() },
            "The name of a parameter can't be empty"
        )
    }
}