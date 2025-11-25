package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PathValidationTest {

    @ParameterizedTest(name = "Test file name with null byte: {0}")
    @ValueSource(strings = ["my_file\u0000.dart", "\u0000file\u0000name\u0000.dart", "valid_name\u0000malicious.dart"])
    fun `test validate file name which contains a null byte`() {
        val fileNameWithNullByte = "my_file\u0000.dart"

        val exception = assertThrows<IllegalArgumentException> {
            PathValidation.validateFileName(fileNameWithNullByte)
        }

        assertTrue(exception.message?.contains("null byte") == true)
    }


    @ParameterizedTest(name = "Test blocked file names: {0}")
    @ValueSource(strings = ["..", "/", "\\"])
    fun `test validate file name for different blocked file names`(invalidFileName: String) {
        val exception = assertThrows<IllegalArgumentException> {
            PathValidation.validateFileName(invalidFileName)
        }

        assertTrue(exception.message?.contains("invalid path traversal") == true)
    }

    @ParameterizedTest(name = "Test valid file name: {0}")
    @ValueSource(
        strings = [
            "my_model.dart",
            "user_repository",
            "data_source",
            "my_widget.dart",
            "test123",
            "file_with_underscores_123"
        ]
    )
    fun `test valid file names`(validFileName: String) {
        PathValidation.validateFileName(validFileName)
    }
}