package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("Test file naming convention")
class FileConventionNamingTest {

    @ParameterizedTest(name = "Test valid name pattern: {arguments}")
    @ValueSource(strings = ["item_model", "item_model.dart", "model.dart", "boss_bar_colour_meep", "test"])
    fun `test name pattern`(pattern: String) {
        assertTrue { isDartConventionFileName(pattern) }
    }

    @ParameterizedTest(name = "Test invalid name pattern: {arguments}")
    @ValueSource(strings = ["hello__world.dart", "_hello__world_.dart", "_test", "model_", "", "Dart_FILE"])
    fun `test invalid file patterns`(pattern: String) {
        assertFalse { isDartConventionFileName(pattern) }
    }
}
