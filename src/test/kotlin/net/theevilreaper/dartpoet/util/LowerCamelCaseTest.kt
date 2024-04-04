package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("Test the utility function to test if a string is in lowercase format")
class LowerCamelCaseTest {

    companion object {

        @JvmStatic
        private fun testSuccessCamelCases() = Stream.of(
            Arguments.of("value"),
            Arguments.of("value1"),
        )

        @JvmStatic
        private fun testInvalidCamelCases() = Stream.of(
            Arguments.of("TEST_VALUE"),
            Arguments.of("NiceValue"),
            Arguments.of("12121121"),
        )
    }

    @ParameterizedTest(name = "Test argument {arguments}")
    @MethodSource("testSuccessCamelCases")
    fun `test successfully lower camel case subjects`(input: String) {
        assertTrue { isInLowerCamelCase(input) }
    }

    @ParameterizedTest(name = "Test argument {arguments}")
    @MethodSource("testInvalidCamelCases")
    fun `test invalid lower camcel case subjects`(input: String) {
        assertFalse { isInLowerCamelCase(input) }
    }
}
