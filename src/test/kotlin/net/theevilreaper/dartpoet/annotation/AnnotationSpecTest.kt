package net.theevilreaper.dartpoet.annotation

import net.theevilreaper.dartpoet.type.ClassName
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AnnotationSpecTest {

    private val expectedAnnotation = "@Override"

    @Test
    fun `test simple annotation without content`() {
        val annotation = AnnotationSpec.builder(Override::class).build()
        assertEquals(expectedAnnotation, annotation.toString())
    }

    @Test
    fun `test toBuilder function`() {
        val className = ClassName("ignore")
        val annotationSpec = AnnotationSpec.builder(className)
            .content("ignore", "true")
            .build()
        val specAsBuilder = annotationSpec.toBuilder()
        assertEquals(annotationSpec.typeName, className)
        assertContentEquals(annotationSpec.content, specAsBuilder.content)
    }
}
