package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
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

        @JvmStatic
        private fun propertiesWithAnnotations() = Stream.of(
            Arguments.of(
                EnumPropertySpec.builder("test")
                    .annotations(AnnotationSpec.builder("jsonIgnore").build()).build(),
                """
                @jsonIgnore
                test
                """.trimIndent()
            ),
            Arguments.of(
                EnumPropertySpec.builder("test")
                    .annotations(
                        AnnotationSpec.builder("jsonIgnore").build(),
                        AnnotationSpec.builder("JsonKey")
                            .content("name: %C", "test").build()
                    ).build(),
                """
                @jsonIgnore
                @JsonKey(name: 'test')
                test
                """.trimIndent()
            )
        )
    }

    @ParameterizedTest
    @MethodSource("propertiesWithAnnotations")
    fun `test property generation with annotations`(propertySpec: EnumPropertySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }

    @ParameterizedTest
    @MethodSource("properties")
    fun `test property generation`(propertySpec: EnumPropertySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }
}