package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("Test lower camel case subjects")
class LowerCamelCaseTest {

    @ParameterizedTest(name = "Test argument {arguments}")
    @ValueSource(strings = ["value", "value1"])
    fun `test successfully lower camel case subjects`(input: String) {
        assertTrue { isInLowerCamelCase(input) }
    }

    @ParameterizedTest(name = "Test argument {arguments}")
    @ValueSource(strings = ["TEST_VALUE", "NiceValue", "12121121"])
    fun `test invalid lower camel case subjects`(input: String) {
        assertFalse { isInLowerCamelCase(input) }
    }
}
