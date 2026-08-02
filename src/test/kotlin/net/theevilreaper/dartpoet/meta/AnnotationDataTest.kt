package net.theevilreaper.dartpoet.meta

import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Test behaviour of the AnnotationData structure")
class AnnotationDataTest {

    private var annotationData: AnnotationData = AnnotationData()

    @Test
    fun `test annotation add`() {
        this.annotationData.annotation(AnnotationSpec.builder("jsonKey").build())
        this.annotationData.annotation { AnnotationSpec.builder("jsonIgnore").build() }
        assertEquals(2, this.annotationData.annotations.size)
    }

    @Test
    fun `test annotations vararg add`() {
        this.annotationData.annotations(
            AnnotationSpec.builder("jsonKey").build(),
            AnnotationSpec.builder("jsonIgnore").build()
        )
        assertEquals(2, this.annotationData.annotations.size)
    }
}
