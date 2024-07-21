package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.directive.impl.DartDirective
import net.theevilreaper.dartpoet.util.DirectiveOrdering
import net.theevilreaper.dartpoet.util.EMPTY_STRING
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@DisplayName("Test directive ordering")
class DirectiveSortTest {

    companion object {

        @JvmStatic
        private fun dartDirectiveArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                listOf(
                    DirectiveFactory.create(DirectiveType.IMPORT, "dart:io"),
                    DirectiveFactory.create(DirectiveType.IMPORT, "dart:math"),
                    DirectiveFactory.create(DirectiveType.PART, "test.dart"),
                ),
                listOf(
                    "dart:io",
                    "dart:math"
                )
            )
        )

        @JvmStatic
        private fun directiveSortArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                listOf(
                    DirectiveFactory.create(DirectiveType.IMPORT, "testD.dart"),
                    DirectiveFactory.create(DirectiveType.IMPORT, "testA.dart"),
                    DirectiveFactory.create(DirectiveType.IMPORT, "testB.dart"),
                    DirectiveFactory.create(DirectiveType.IMPORT, "testC.dart"),
                ),
                listOf(
                    "testA.dart",
                    "testB.dart",
                    "testC.dart",
                    "testD.dart",
                )
            )
        )
    }

    @ParameterizedTest
    @MethodSource("dartDirectiveArguments")
    fun `test dart directive sort`(directives: List<DartDirective>, expected: List<String>) {
        val sortedData = DirectiveOrdering.sortDirectives<DartDirective>(DartDirective::class, directives)
        assertNotEquals(sortedData.size, directives.size)
        val trimmedData = formatImports(sortedData, "import '", "';")
        assertEquals(expected.size, sortedData.size)
        assertEquals(expected, trimmedData)
    }

    @ParameterizedTest
    @MethodSource("directiveSortArguments")
    fun `test package directive sort`(directives: List<DartDirective>, expected: List<String>) {
        val sortedData = DirectiveOrdering.sortDirectives(directives)
        val trimmedData = formatImports(sortedData, "import 'package:", "';")
        assertEquals(expected.size, sortedData.size)
        assertEquals(expected, trimmedData)
    }

    @Test
    fun `test empty ordering result`() {
        val sortedData = DirectiveOrdering.sortDirectives(listOf())
        assertEquals(emptyList(), sortedData)
        assertEquals(emptyList(), DirectiveOrdering.sortDirectives(DartDirective::class, listOf()))
    }

    private inline fun <reified T : Directive> formatImports(
        directives: List<T>,
        vararg placeholders: String,
    ): List<String> {
        return directives.map {
            var directive: String = it.asString()
            for (placeholder in placeholders) {
                directive = directive.replace(placeholder, EMPTY_STRING)
            }
            return@map directive
        }.toList()
    }
}
