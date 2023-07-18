package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.directive.*
import net.theevilreaper.dartpoet.util.filterAndSort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DartFileImportOrderingTest {

    companion object {

        @JvmStatic
        private fun importData()  = Stream.of(
            Arguments.of(
                listOf(DartDirective("model_b"), DartDirective("model_c"), DartDirective("model_a")),
                DartDirective::class
            )
        )

        @JvmStatic
        private fun testData() = Stream.of(
            Arguments.of(
                listOf(
                    DartDirective("model_a"),
                    DartDirective("model_d"),
                    RelativeDirective("dart:html"),
                    LibraryDirective("test")
                ),
                LibraryDirective::class
            )
        )
    }

    @ParameterizedTest
    @MethodSource("importData")
    fun `test import sort`(imports: List<DartDirective>, importClass: KClass<DartDirective>) {
        val rootImports = mutableListOf<Directive>()
        rootImports.addAll(imports)
        rootImports.add(RelativeDirective("test"))
        assertEquals(4, rootImports.size)
        val sortedImports: List<DartDirective> = rootImports.toList().filterAndSort<DartDirective>() { it.toString() }
        assertEquals(3, sortedImports.size)

        sortedImports.forEach {
            assertEquals(importClass, it::class)
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun `test import with all imports implementation in a list`(imports: List<DartDirective>, importClass: KClass<LibraryDirective>) {
        val rootImports = mutableListOf<Directive>()
        rootImports.addAll(imports)
        repeat(4) {
            rootImports.add(DartDirective("test_$it"))
        }
        assertEquals(8, rootImports.size)
        assertTrue { rootImports.filterAndSort<ExportDirective> { it.toString() }.isEmpty() }

        val libraryImport = rootImports.filterAndSort<LibraryDirective> { it.toString() }
        assertFalse { libraryImport.isEmpty() }

        val templateLibDirective = rootImports[3]

        assertEquals(importClass, libraryImport.first()::class)
        assertEquals(templateLibDirective, libraryImport.first())
    }
}