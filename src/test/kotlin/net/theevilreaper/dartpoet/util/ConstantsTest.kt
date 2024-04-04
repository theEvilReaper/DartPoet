package net.theevilreaper.dartpoet.util

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConstantsTest {

    companion object {

        @JvmStatic
        private fun validPatterns() = Stream.of(
            Arguments.of("item_model"),
            Arguments.of("item_model.dart"),
            Arguments.of("model.dart"),
            Arguments.of("boss_bar_colour_meep"),
            Arguments.of("test"),
        )

        @JvmStatic
        private fun invalidPatterns() = Stream.of(
            Arguments.of("hello__world.dart"),
            Arguments.of("_hello__world_.dart"),
            Arguments.of("_test"),
            Arguments.of("model_"),
            Arguments.of(""),
            Arguments.of("Dart_FILE"),
        )
    }

    @ParameterizedTest(name = "Test valid name pattern: {arguments}")
    @MethodSource("validPatterns")
    fun `test name pattern`(pattern: String) {
        assertTrue { isDartConventionFileName(pattern) }
    }

    @ParameterizedTest(name = "Test invalid name pattern: {arguments}")
    @MethodSource("invalidPatterns")
    fun `test invalid file patterns`(pattern: String) {
        assertFalse { isDartConventionFileName(pattern) }
    }
}
