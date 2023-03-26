package net.theevilreaper.dartpoet.util

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConstantsTest {

    @Test
    fun `test name pattern`() {
        assertFalse { isDartConventionFileName("") }
        assertFalse { isDartConventionFileName("Dart_FILE") }
        assertTrue { isDartConventionFileName("item_model") }
        assertTrue { isDartConventionFileName("item_model.dart") }
    }
}