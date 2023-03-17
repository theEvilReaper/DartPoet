package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.import.PartImport
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PartImportTest {

    private val expectedImport = "part 'item_model.freezed.dart';"

    @Test
    fun `create part import`() {
        val partImport = PartImport("item_model.freezed.dart")
        assertEquals(expectedImport, partImport.toString())
    }
}