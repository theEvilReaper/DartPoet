package net.theevilreaper.dartpoet.util

import net.theevilreaper.dartpoet.directive.DartDirective
import net.theevilreaper.dartpoet.directive.Directive
import net.theevilreaper.dartpoet.directive.LibraryDirective
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class DirectiveImportFilterTest {

    companion object {

        @JvmStatic
        private fun testSortToDartDirective() = Stream.of(
            Arguments.of(
                listOf(
                    DartDirective("dart:math"),
                    DartDirective("dart:html"),
                    LibraryDirective("test")
                ),
                DartDirective::class.java,
                listOf(
                    DartDirective("dart:html"),
                    DartDirective("dart:math"),
                ),
            )
        )
    }

    @ParameterizedTest
    @MethodSource("testSortToDartDirective")
    fun `test import sort to dart directive`(
        directives: List<Directive>,
        castClass: Class<DartDirective>,
        expectedOrder: List<DartDirective>
    ) {
        assertEquals(3, directives.size)
        val sortedDirectives =
            directives.filterByImplementation(classType = castClass) { directive: Directive -> directive.toString() }
        assertEquals(2, sortedDirectives.size)
        for (sortedDirective in sortedDirectives) {
            assertEquals(DartDirective::class, sortedDirective::class)
        }
        assertIterableEquals(expectedOrder, sortedDirectives)
    }
}