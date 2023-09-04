package net.theevilreaper.dartpoet.extension

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExtensionSpecTest {

    @Test
    fun `test spec to builder conversation`() {
        val extensionSpec = ExtensionSpec.builder("isEmpty", "String")
            .endsWithNewLine(true)
            .doc("%C", "This is a test line")
            .build()
        val specAsBuilder = extensionSpec.toBuilder()
        assertEquals(extensionSpec.name, specAsBuilder.name)
        assertEquals(extensionSpec.extClass, specAsBuilder.extClass)
        assertTrue { specAsBuilder.docs.isNotEmpty() }
        assertEquals(extensionSpec.endWithNewLine, specAsBuilder.endWithNewLine)
    }
}