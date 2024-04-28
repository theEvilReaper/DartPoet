package net.theevilreaper.dartpoet.directive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Test part directive creation")
class PartDirectiveTest {

    private val expectedImport = "part 'item_model.freezed.dart';"

    @Test
    fun `create part import`() {
        val partImport = DirectiveFactory.create(DirectiveType.PART, "item_model.freezed.dart")
        assertEquals(expectedImport, partImport.asString())
    }
}
