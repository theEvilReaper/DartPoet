package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 */

class ParameterizedTypeNameTest {

    companion object {

        @JvmStatic
        private fun typeNameTest() = Stream.of(
            Arguments.of("List<int>", List::class.parameterizedBy(Int::class)),
            Arguments.of(
                "Map<String, String>",
                Map::class.parameterizedBy(String::class, String::class)
            ),
            Arguments.of(
                "Map<String, dynamic>",
                Map::class.parameterizedBy(String::class.asTypeName(), DynamicClassName())
            )
        )
    }

    @ParameterizedTest
    @MethodSource("typeNameTest")
    fun `test parameterized type name class`(expected: String, parameter: ParameterizedTypeName) {
        assertEquals(expected, parameter.toString())
    }
}