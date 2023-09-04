package net.theevilreaper.dartpoet.annotation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AnnotationSpecTest {

    private val expectedAnnotation = "@jsonIgnore"

    @Test
    fun `test annotation with empty name`() {
        assertThrows(
            IllegalStateException::class.java,
            { AnnotationSpec.builder("").build() },
            "The name can't be empty"
        )
        assertThrows(
            IllegalStateException::class.java,
            { AnnotationSpec.builder(" ").build() },
            "The name can't be empty"
        )
    }

    @Test
    fun `test simple annotation without content`() {
        val annotation = AnnotationSpec.builder("jsonIgnore").build()
        assertEquals(expectedAnnotation, annotation.toString())
    }

    @Test
    fun `test toBuilder function`() {
        val annotationSpec = AnnotationSpec.builder("jsonIgnore")
            .content("ignore", "true")
            .build()
        val specAsBuilder = annotationSpec.toBuilder()
        assertEquals(annotationSpec.name, specAsBuilder.name)
        assertContentEquals(annotationSpec.content, specAsBuilder.content)
    }
}
