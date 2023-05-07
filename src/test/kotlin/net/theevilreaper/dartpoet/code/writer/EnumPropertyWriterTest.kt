package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.enum.EnumPropertySpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class EnumPropertyWriterTest {

    companion object {

        @JvmStatic
        private fun properties() = Stream.of(
            Arguments.of(EnumPropertySpec.builder("test").generic("String").build(), "test<String>"),
            Arguments.of(EnumPropertySpec.builder("test")
                .parameter("%C", "/dash")
                .build(),
                "test('/dash')"
            ),
            Arguments.of(EnumPropertySpec.builder("test")
                .parameter("%L", "10")
                .build(),
                "test(10)"
            ),
            Arguments.of(EnumPropertySpec.builder("dashboard")
                .parameter("%C", "Dashboard")
                .parameter("%C", "/dashboard")
                .parameter("%L", "false")
                .build(),
                "dashboard('Dashboard', '/dashboard', false)"
            ),

        )
    }

    @ParameterizedTest
    @MethodSource("properties")
    fun `test property generation`(propertySpec: EnumPropertySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }
}