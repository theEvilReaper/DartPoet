package net.theevilreaper.dartpoet

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import net.theevilreaper.dartpoet.property.DartPropertySpec
import org.junit.Test

class DartClassBuilderTest {

    private val className: String = "DartClass"

    @Test
    fun `test simple class creation`() {
        val clazz =
            DartClassSpec.builder(className).annotation(AnnotationSpec()).modifier { DartModifier.FINAL }.build()

        assertThat(clazz.toString()).isEqualTo("""
            public class $className {
            }
        """.trimIndent())
    }

    @Test
    fun `test enum class`() {
        val clazz = DartClassSpec.enumClass(className).build()
        assertThat(clazz.toString()).isEqualTo("""
            enum $className {
            }
        """.trimIndent())
    }

    @Test
    fun `test mixin class`() {
        val clazz = DartClassSpec.mixinClass(className).build()
        assertThat(clazz.toString()).isEqualTo("""
            mixin $className {
            }
        """.trimIndent()
        )
    }

    @Test
    fun `test class with a parameter`() {
        val clazz = DartClassSpec.builder(className)
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