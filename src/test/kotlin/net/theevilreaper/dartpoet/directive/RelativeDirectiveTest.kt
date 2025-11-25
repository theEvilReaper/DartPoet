package net.theevilreaper.dartpoet.directive

import net.theevilreaper.dartpoet.directive.impl.RelativeDirective
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.regex.Pattern
import java.util.stream.Stream
import kotlin.test.assertTrue

class RelativeDirectiveTest {

    private val countPattern = Pattern.compile("""^((\.\./)+)""")
    private val testRelativePath = "item_model.dart"

    companion object {

        private const val TEST_IMPORT = "../model/item_model.dart"
        private const val CAST_VALUE = "item"

        @JvmStatic
        private fun relativeDirectives() = Stream.of(
            Arguments.of(RelativeDirective(TEST_IMPORT), "import '../../model/item_model.dart';"),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.AS, CAST_VALUE),
                "import '../../model/item_model.dart' as item;"
            ),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.DEFERRED, CAST_VALUE),
                "import '../../model/item_model.dart' deferred as item;"
            ),
            Arguments.of(
                RelativeDirective(TEST_IMPORT, CastType.HIDE, CAST_VALUE),
                "import '../../model/item_model.dart' hide item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT),
                "import '../../model/item_model.dart';"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.AS, CAST_VALUE),
                "import '../../model/item_model.dart' as item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.DEFERRED, CAST_VALUE),
                "import '../../model/item_model.dart' deferred as item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.HIDE, CAST_VALUE),
                "import '../../model/item_model.dart' hide item;"
            ),
            Arguments.of(
                DirectiveFactory.create(DirectiveType.RELATIVE, TEST_IMPORT, CastType.SHOW, CAST_VALUE),
                "import '../../model/item_model.dart' show item;"
            )
        )
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

    @ParameterizedTest
    @MethodSource("relativeDirectives")
    fun `test relative dart imports`(current: Directive, expected: String) {
        val currentImportString = current.asString()
        assertEquals(expected, currentImportString)
        val prefixCount = countRelativeSegments(currentImportString)
        assertEquals(2, prefixCount, "There should be exactly two dot prefix")
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