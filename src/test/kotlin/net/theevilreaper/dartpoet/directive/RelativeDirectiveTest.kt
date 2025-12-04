package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.directive.impl.RelativeDirective
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.regex.Pattern
import java.util.stream.Stream

class RelativeDirectiveTest {

    private val countPattern = Pattern.compile("""^((\.\./)+)""")
    private val testRelativePath = "item_model.dart"

    companion object {

        private const val TEST_IMPORT = "model/item_model.dart"
        private const val CAST_VALUE = "item"

        @JvmStatic
        private fun relativeDirectives() = Stream.of(
            Arguments.of(RelativeDirective(TEST_IMPORT, depth = 2), "import '../../model/item_model.dart';"),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.AS, CAST_VALUE, depth = 2),
                "import '../../model/item_model.dart' as item;"
            ),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.DEFERRED, CAST_VALUE, depth = 2),
                "import '../../model/item_model.dart' deferred as item;"
            ),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.HIDE, CAST_VALUE, depth = 2),
                "import '../../model/item_model.dart' hide item;"
            ),
        )

        @JvmStatic
        private fun relativeDirectivesWithFactoryCalls() = Stream.of(
            Arguments.of(
                DirectiveFactory.createRelative( TEST_IMPORT, depth = 2),
                "import '../../model/item_model.dart';",
                2
            ),
            Arguments.of(
                DirectiveFactory.createRelative(TEST_IMPORT, CastType.AS, CAST_VALUE, depth = 2),
                "import '../../model/item_model.dart' as item;",
                2
            ),
            Arguments.of(
                DirectiveFactory.createRelative(TEST_IMPORT, CastType.DEFERRED, CAST_VALUE, depth = 2),
                "import '../../model/item_model.dart' deferred as item;",
                2
            ),
            Arguments.of(
                DirectiveFactory.createRelative(TEST_IMPORT, CastType.HIDE, CAST_VALUE),
                "import '../model/item_model.dart' hide item;",
                1
            ),
            Arguments.of(
                DirectiveFactory.createRelative(TEST_IMPORT, CastType.SHOW, CAST_VALUE),
                "import '../model/item_model.dart' show item;",
                1
            )
        )

        @JvmStatic
        private fun invalidPrefixPatterns() = Stream.of(
            Arguments.of("./item_model.dart", 1, "import '../item_model.dart';"),
            Arguments.of(".../item_model.dart", 1, "import '../item_model.dart';"),
            Arguments.of("..../item_model.dart", 2, "import '../../item_model.dart';"),
            Arguments.of("../item_model.dart", 1, "import '../item_model.dart';"), // already valid
            Arguments.of("../../item_model.dart", 1, "import '../../item_model.dart';"), // keeps existing
        )
    }

    @ParameterizedTest(name = "test relative directives without the factory")
    @MethodSource("relativeDirectives")
    fun `test relative dart imports with a depth of two`(current: Directive, expected: String) {
        val currentImportString = current.asString()
        assertEquals(expected, currentImportString)
        val prefixCount = countRelativeSegments(currentImportString)
        assertEquals(2, prefixCount, "There should be exactly two dot prefix")
    }

    @ParameterizedTest(name = "test relative directives with the factory with depth {2}")
    @MethodSource("relativeDirectivesWithFactoryCalls")
    fun `test relative imports with the factory and variable depth`(current: Directive, expected: String, depth: Int) {
        val currentImportString = current.asString()
        assertEquals(expected, currentImportString)
        val prefixCount = countRelativeSegments(currentImportString)
        assertEquals(depth, prefixCount, "There should be exactly two dot prefix")
    }

    @ParameterizedTest(name = "Sanitize invalid prefix: {0} with depth {1}")
    @MethodSource("invalidPrefixPatterns")
    fun `test sanitization of invalid relative prefixes`(
        input: String,
        depth: Int,
        expected: String
    ) {
        val relativeImport = RelativeDirective(input, depth = depth)
        assertEquals(expected, relativeImport.asString())
    }

    @Test
    fun `test relative import with invalid depth`() {
        assertThrowsExactly(
            IllegalArgumentException::class.java,
            { RelativeDirective(testRelativePath, depth = -1) },
            "The depth of a relative import can't be negative"
        )
    }

    @Test
    fun `test relative import without given dots`() {
        val import = DirectiveFactory.create(DirectiveType.RELATIVE, "item_model.dart")
        assertNotNull(import)
        assertInstanceOf(RelativeDirective::class.java, import)

        val importAsString = import.asString()
        println(importAsString)
        assertNotNull(importAsString, "The import should not be null")

        val prefixCount = countRelativeSegments(importAsString)
        assertEquals(1, prefixCount, "There should be exactly one dot prefix")
        assertTrue(importAsString.contains("../"))
        assertEquals("import '../item_model.dart';", importAsString)
    }

    @Test
    fun `test relative import with a different relative depth than one`() {
        val relativeImport = RelativeDirective(testRelativePath, depth = 4)
        assertNotNull(relativeImport)
        assertEquals(4, relativeImport.depth)

        val importAsString = relativeImport.asString()

        val prefixCount = countRelativeSegments(importAsString)
        assertEquals(4, prefixCount, "There should be exactly two dot prefix")
    }

    @Test
    fun `test relative import with prefix dots`() {
        val relativeImport = RelativeDirective("../$testRelativePath")
        assertNotNull(relativeImport)
        assertEquals(1, relativeImport.depth)

        val importAsString = relativeImport.asString()
        assertEquals("import '../$testRelativePath';", importAsString)
    }

    /**
     * Counts the number of relative segments in the given path.
     * @param path the path to count the segments for
     * @return the number of relative segments in the path
     */
    private fun countRelativeSegments(path: String): Int {
        val cleaned = path.substringAfter("import ").substringBefore(" ").trim('\'', '"', ';')

        val matcher = countPattern.matcher(cleaned)
        if (!matcher.find()) return 0

        return matcher.group(1).length / matcher.group(2).length
    }
}