package net.theevilreaper.dartpoet.code.writer

import com.google.common.truth.Truth.assertThat
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.EnumSpec
import net.theevilreaper.dartpoet.extension.type.ExtensionTypeSpec
import net.theevilreaper.dartpoet.mixin.MixinSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.spec.TypeSpec
import net.theevilreaper.dartpoet.type.DOUBLE
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

        private val documentedEnum = EnumSpec.builder("Color")
            .doc("All supported colors.")
            .annotation(AnnotationSpec.builder("deprecated").build())
            .entry(EnumEntrySpec.builder("red").build())
            .build()

        private val documentedMixin = MixinSpec.builder("Walkable")
            .doc("Adds walking behaviour.")
            .annotation(AnnotationSpec.builder("deprecated").build())
            .build()

        private val documentedExtensionType = ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
            .doc("A distance in meters.")
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
            ),
            Arguments.of(
                documentedEnum,
                """
                |/// All supported colors.
                |@deprecated
                |enum Color {
                |  red
                |}
                """.trimMargin()
            ),
            Arguments.of(
                documentedMixin,
                """
                |/// Adds walking behaviour.
                |@deprecated
                |mixin Walkable {}
                """.trimMargin()
            ),
            Arguments.of(
                documentedExtensionType,
                """
                |/// A distance in meters.
                |extension type Meters(double value) {}
                """.trimMargin()
            )
        )

        @JvmStatic
        private fun toBuilderRoundtrips() = Stream.of(
            Arguments.of(documentedClass, documentedClass.toBuilder().build()),
            Arguments.of(documentedEnum, documentedEnum.toBuilder().build()),
            Arguments.of(documentedMixin, documentedMixin.toBuilder().build()),
            Arguments.of(documentedExtensionType, documentedExtensionType.toBuilder().build())
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
