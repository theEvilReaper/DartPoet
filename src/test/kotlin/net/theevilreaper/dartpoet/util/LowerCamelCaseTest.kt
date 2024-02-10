package net.theevilreaper.dartpoet.util

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LowerCamelCaseTest {

    companion object {

        @JvmStatic
        private fun testSubjects() = Stream.of(
            Arguments.of("TEST_VALUE", false),
            Arguments.of("value", true),
            Arguments.of("NiceValue", false),
            Arguments.of("12121121", false),
            Arguments.of("value1", true)
        )
    }

    @ParameterizedTest
    @MethodSource("testSubjects")
    fun `test lower camel case subjects`(input: String, expected: Boolean) {
        if (expected) {
            assertTrue(isInLowerCamelCase(input))
        } else {
            assertFalse(isInLowerCamelCase(input))
        }
    }
}