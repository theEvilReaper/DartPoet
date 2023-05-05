package net.theevilreaper.dartpoet.enumeration

import com.google.common.truth.Truth
import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import net.theevilreaper.dartpoet.parameter.DartParameterSpec
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class EnumPropertySpecTest {

    companion object {

        @JvmStatic
        private fun values() = Stream.of(
            Arguments.of(EnumPropertySpec.builder("test").build(), "test"),
            Arguments.of(EnumPropertySpec.builder("test").generic("String").build(), "test<String>"),
            Arguments.of(
                EnumPropertySpec.builder("navigation")
                    .parameter("%C", "/dashboard")
                    .build(),
                "navigation('/dashboard')"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("values")
    fun `test enum property generation`(spec: EnumPropertySpec, excepted: String) {
        Truth.assertThat(spec.toString()).isEqualTo(excepted)
    }

    @Test
    fun `test simple enum value`() {
        val enumValue = EnumPropertySpec.builder("test").build()

    }
}