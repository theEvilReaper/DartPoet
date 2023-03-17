package net.theevilreaper.dartpoet.clazz

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartClassType
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.property.DartPropertySpec
import org.junit.Test

class DartClassBuilderTest {

    private val className: String = "DartClass"

    @Test
    fun `test simple class creation`() {
        val clazz =
            DartClassSpec.builder(className).annotation(AnnotationSpec()).modifier { DartModifier.FINAL }.build()

        assertThat(clazz.toString()).isEqualTo("""
            public final class $className {
            }
        """.trimIndent())
    }

    @Test
    fun `test class with a parameter`() {
        val clazz = DartClassSpec.builder(className, DartClassType.CLASS)
            .property(DartPropertySpec
                .builder("name", "String")
                .nullable(false)
                .build()
            )
            .build()

        assertThat(clazz.toString()).isEqualTo(
            """
                public class $className {
                  
                  String name;
                }
                
            """.trimIndent()
        )
    }
}