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
            { PartDirective(" ") },
            "The path of an Import can't be empty"
        )
        Assertions.assertThrows(
            IllegalStateException::class.java,
            { PartDirective("") },
            "The path of an Import can't be empty"
        )
    }

    @Test
    fun `create part import`() {
        val partImport = PartDirective("item_model.freezed.dart")
        assertEquals(expectedImport, partImport.asString())
    }
}