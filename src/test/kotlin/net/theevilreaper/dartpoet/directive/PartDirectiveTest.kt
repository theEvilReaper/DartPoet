package net.theevilreaper.dartpoet.directive

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PartDirectiveTest {

    private val expectedImport = "part 'item_model.freezed.dart';"

    @Test
    fun `test import with empty path`() {
        Assertions.assertThrows(
            IllegalStateException::class.java,
            { DirectiveFactory.create(DirectiveType.IMPORT, " ") },
            "The path of an Import can't be empty"
        )
        Assertions.assertThrows(
            IllegalStateException::class.java,
            { DirectiveFactory.create(DirectiveType.RELATIVE, " ") },
            "The path of an Import can't be empty"
        )
    }

    @Test
    fun `create part import`() {
        val partImport = DirectiveFactory.create(DirectiveType.PART, "item_model.freezed.dart")
        assertEquals(expectedImport, partImport.asString())
    }
}