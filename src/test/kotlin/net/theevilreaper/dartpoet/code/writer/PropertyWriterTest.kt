package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.property.DartPropertySpec
import org.junit.jupiter.api.Test

class PropertyWriterTest {

    @Test
    fun `write simple variable without initializer`() {
        val parameter = DartPropertySpec.builder("id", "int").build()
        assertThat(parameter.toString()).isEqualTo(
            """
            int id;
            """.trimIndent()
        )
    }

    @Test
    fun `write simple nullable variable without initializer`() {
        val parameter = DartPropertySpec.builder("id", "String")
            .nullable(true)
            .build()
        assertThat(parameter.toString()).isEqualTo(
            """
            String? id;
            """.trimIndent()
        )
    }

    @Test
    fun `write simple private nullable variable without initializer`() {
        val parameter = DartPropertySpec.builder("test", "String")
            .modifier { DartModifier.PRIVATE }
            .nullable(true)
            .build()
        assertThat(parameter.toString()).isEqualTo(
            """
            String? _test;
            """.trimIndent()
        )
    }

    @Test
    fun `write simple variable with late as keyword`() {
        val parameter = DartPropertySpec.builder("abc", "String")
            .modifier { DartModifier.LATE }
            .build()
        assertThat(parameter.toString()).isEqualTo(
            """
            late String abc;
            """.trimIndent()
        )
    }

    @Test
    fun `write simple variable with initializer`() {
        val parameter = DartPropertySpec.builder("age", "int")
            .initWith("%L", "12")
            .build()
        assertThat(parameter.toString()).isEqualTo(
            """
            int age = 12;
            """.trimIndent()
        )
    }

    @Test
    fun `write simple variable with one annotation`() {
        val parameter = DartPropertySpec.builder("age", "int")
            .annotation { AnnotationSpec.builder("jsonIgnore").build() }
            .initWith("%L", "12")
            .build()
        assertThat(parameter.toString()).isEqualTo(
            """
            @jsonIgnore
            int age = 12;
            """.trimIndent()
        )
    }

    @Test
    fun `write parameter with annotations`() {
        val param = DartPropertySpec.builder("description", "String")
            .nullable(true)
            .annotation {
                AnnotationSpec.builder("JsonKey")
                    .content("name: %C", "description")
                    .build()
            }
            .build()
        assertThat(param.toString()).isEqualTo(
            """
            @JsonKey(name: 'description')
            String? description;
            """.trimIndent()
        )
    }
}