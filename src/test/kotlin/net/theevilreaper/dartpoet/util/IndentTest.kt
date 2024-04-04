package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("Test if a given indent is a valid indent for the project")
class IndentTest {

    @ParameterizedTest(name = "Test valid indent value: {arguments}")
    @ValueSource(strings = [" ", "  "])
    fun `test valid indent pattern`(indent: String) {
        assertTrue { isIndent(indent) }
    }

    @ParameterizedTest(name = "Test invalid indent value: {arguments}")
    @ValueSource(strings = ["", " a", "123"])
    fun `test invalid indent patterns`(indent: String) {
        assertFalse { isIndent(indent) }
    }
}
