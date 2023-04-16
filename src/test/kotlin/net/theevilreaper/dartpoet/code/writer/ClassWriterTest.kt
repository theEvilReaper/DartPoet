package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.DartClassSpec
import org.junit.jupiter.api.Test

class ClassWriterTest {

    @Test
    fun `write simple class without any parameters etc`() {
        val clazz = DartClassSpec.builder("Test").build()
        assertThat(clazz.toString()).isEqualTo(
            """
            class Test {
            }
            """.trimIndent()
        )

    }

    @Test
    fun `write mixin class without any parameters etc`() {
        val clazz = DartClassSpec.mixinClass("Test")
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            mixin Test {
            }
            """.trimIndent()
        )

    }

    @Test
    fun `write enum class without any parameters etc`() {
        val clazz = DartClassSpec.enumClass("Test")
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            enum Test {
            }
            """.trimIndent()
        )
    }

    @Test
    fun `write class which ends with empty line`() {
        val clazz = DartClassSpec.builder("Model")
            .endWithNewLine(true)
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            class Model {
            }
            
            """.trimIndent()
        )
    }

    @Test
    fun `write abstract class without any content`() {
        val clazz = DartClassSpec.abstractClass("DatabaseHandler")
            .endWithNewLine(true)
            .build()
        assertThat(clazz.toString()).isEqualTo(
            """
            abstract class DatabaseHandler {
            }
            
            """.trimIndent()
        )
    }

}