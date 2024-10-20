package net.theevilreaper.dartpoet.code.writer

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.parameter.EnumParameterSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test enum property writer")
class EnumEntrySpecTest {

    companion object {

        @JvmStatic
        private fun properties() = Stream.of(
            Arguments.of(EnumEntrySpec.builder("test").generic(String::class).build(), "test<String>"),
            Arguments.of(
                EnumEntrySpec.builder("test")
                    .parameter(
                        EnumParameterSpec.from("%C", "/dash")
                    )
                    .build(),
                "test('/dash')"
            ),
            Arguments.of(
                EnumEntrySpec.builder("test")
                    .parameter(
                        EnumParameterSpec.from("%L", "10")
                    )
                    .build(),
                "test(10)"
            ),
            Arguments.of(
                EnumEntrySpec.builder("dashboard")
                    .parameter(EnumParameterSpec.from("%C", "Dashboard"))
                    .parameter(EnumParameterSpec.from("%C", "/dashboard"))
                    .parameter(EnumParameterSpec.from("%L", "false"))
                    .build(),
                "dashboard('Dashboard', '/dashboard', false)"
            ),
        )

        @JvmStatic
        private fun propertiesWithAnnotations() = Stream.of(
            Arguments.of(
                EnumEntrySpec.builder("test")
                    .annotations(AnnotationSpec.builder("jsonIgnore").build()).build(),
                """
                @jsonIgnore
                test
                """.trimIndent()
            ),
            Arguments.of(
                EnumEntrySpec.builder("test")
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
    fun `test property generation with annotations`(propertySpec: EnumEntrySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }

    @ParameterizedTest
    @MethodSource("properties")
    fun `test property generation`(propertySpec: EnumEntrySpec, expected: String) {
        assertEquals(expected, propertySpec.toString())
    }
}