package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ConstantsTest {

    companion object {

        @JvmStatic
        private fun patterns() = Stream.of(
            Arguments.of("", false),
            Arguments.of("Dart_FILE", false),
            Arguments.of("item_model", true),
            Arguments.of("item_model.dart", true),
            Arguments.of("model.dart", true),
            Arguments.of("model_", false),
            Arguments.of("boss_bar_colour_meep", true),
            Arguments.of("hello__world.dart", false),
            Arguments.of("_hello__world_.dart", false),
        )
    }

    @ParameterizedTest
    @MethodSource("patterns")
    fun `test name pattern`(name: String, result: Boolean) {
        assertEquals(result, isDartConventionFileName(name))
    }
}
