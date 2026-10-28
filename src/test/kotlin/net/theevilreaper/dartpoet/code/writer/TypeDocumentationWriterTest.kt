package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Test the documentation of type declarations")
class TypeDocumentationWriterTest {

    companion object {

        private val documentedClass = ClassSpec.builder("Model")
            .doc("This is a model class.")
            .doc("Handles %L.", "requests")
            .annotation(AnnotationSpec.builder("deprecated").build())
            .build()

        @JvmStatic
        private fun documentedTypes() = Stream.of(
            Arguments.of(
                documentedClass,
                """
                |/// This is a model class.
                |/// Handles requests.
                |@deprecated
                |class Model {}
                """.trimMargin()
            )
        )

        @JvmStatic
        private fun toBuilderRoundtrips() = Stream.of(
            Arguments.of(documentedClass, documentedClass.toBuilder().build())
        )
    }

    @ParameterizedTest
    @MethodSource("documentedTypes")
    fun `test documentation is written above the type declaration`(spec: TypeSpec, expected: String) {
        spec.verifyDartOutput(expected)
    }

    @ParameterizedTest
    @MethodSource("toBuilderRoundtrips")
    fun `test documentation survives toBuilder`(original: TypeSpec, rebuilt: TypeSpec) {
        assertThat(rebuilt.toString()).isEqualTo(original.toString())
    }

    @Test
    fun `test anonymous class with documentation throws`() {
        assertThrows<IllegalStateException> {
            ClassSpec.anonymousClassBuilder()
                .doc("Not allowed")
                .build()
        }
    }
}
