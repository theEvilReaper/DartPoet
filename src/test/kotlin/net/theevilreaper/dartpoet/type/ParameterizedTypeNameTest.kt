package net.theevilreaper.dartpoet.type

import net.theevilreaper.dartpoet.type.ParameterizedTypeName.Companion.parameterizedBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test cases for the ParameterizedTypeName implementation")
class ParameterizedTypeNameTest {

    companion object {

        @JvmStatic
        private fun testTypeNamesWithGenerics() = Stream.of(
            Arguments.of("List<int>", List::class.parameterizedBy(Int::class)),
            Arguments.of(
                "Map<String, String>",
                Map::class.parameterizedBy(String::class, String::class)
            ),
            Arguments.of(
                "Map<String, dynamic>",
                Map::class.parameterizedBy(String::class.asTypeName(), DYNAMIC)
            )
        )

        @JvmStatic
        private fun typeNameCompanions() = Stream.of(
            Arguments.of("TestClass<int>", ClassName("TestClass").parameterizedBy(Int::class.asTypeName())),
            Arguments.of("TestClass<int>", ClassName("TestClass").parameterizedBy(Int::class)),
            Arguments.of("List<int>", List::class.java.parameterizedBy(Int::class.java)),
        )

        @JvmStatic
        private fun testEnclosingTyeName() = Stream.of(
            Arguments.of(
                "Map.Entry",
                ParameterizedTypeName(Map::class.asTypeName(), Map.Entry::class.asClassName(), typeArguments = listOf())
            ),
            Arguments.of(
                "Map.Entry<String, double>",
                ParameterizedTypeName(
                    Map::class.asTypeName(),
                    Map.Entry::class.asClassName(),
                    typeArguments = listOf(String::class.asTypeName(), Double::class.asTypeName())
                )
            )
        )
    }

    @ParameterizedTest(name = "Test creation of: {0}")
    @MethodSource("testTypeNamesWithGenerics")
    fun `test parameterized type name class`(expected: String, parameter: ParameterizedTypeName) {
        assertEquals(expected, parameter.toString())
    }

    @ParameterizedTest(name = "Test creation over companion: {0}")
    @MethodSource("typeNameCompanions")
    fun `test method from the parameterized companion object`(expected: String, parameter: ParameterizedTypeName) {
        assertEquals(expected, parameter.toString())
    }

    @ParameterizedTest(name = "Test creation with enclosing name: {0}")
    @MethodSource("testEnclosingTyeName")
    fun `test parameterized write with an enclosingTypeName`(expected: String, parameter: ParameterizedTypeName) {
        assertEquals(expected, parameter.toString())
    }

    @Test
    fun `test copy method from the parameterized TypeName`() {
        val parameterizedTypeName = List::class.parameterizedBy(Int::class)
        assertFalse { parameterizedTypeName.isNullable }
        val nullableType = parameterizedTypeName.copy(nullable = true)
        assertTrue { nullableType.isNullable }
        val changedParameter = parameterizedTypeName.copy(nullable = true, listOf(Double::class.asClassName()))
        assertNotEquals(parameterizedTypeName, changedParameter)
        assertNotEquals(nullableType, changedParameter)
    }
}