package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.annotation.AnnotationSpec
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.enum.EnumEntrySpec
import net.theevilreaper.dartpoet.enum.EnumSpec
import net.theevilreaper.dartpoet.extension.type.ExtensionTypeSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.mixin.MixinSpec
import net.theevilreaper.dartpoet.parameter.ParameterSpec
import net.theevilreaper.dartpoet.type.ClassName
import net.theevilreaper.dartpoet.type.DOUBLE
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for type documentation verified against the Dart analyzer")
class DocumentationCorpusTest {

    @Test
    fun `test documented class, enum, mixin and extension type in DartFile`() {
        val file = DartFile.builder("documented_types")
            .doc("Generated file. Do not edit.")
            .directive(DirectiveFactory.unnamedLib())
            .type(
                EnumSpec.builder("Color")
                    .doc("All supported colors.")
                    .entry(EnumEntrySpec.builder("red").build())
                    .entry(EnumEntrySpec.builder("green").build())
                    .build()
            )
            .type(
                MixinSpec.builder("Describable")
                    .doc("Adds a description to a type.")
                    .function(
                        FunctionSpec.builder("describe")
                            .returns(STRING)
                            .addCode("return 'describable';")
                            .build()
                    )
                    .build()
            )
            .type(
                ClassSpec.builder("Palette")
                    .doc("A collection of colors.")
                    .annotation(AnnotationSpec.builder("deprecated").build())
                    .withMixins(ClassName("Describable"))
                    .build()
            )
            .type(
                ExtensionTypeSpec.builder("Meters", ParameterSpec.positional("value", DOUBLE).build())
                    .doc("A distance in meters.")
                    .build()
            )
            .build()

        file.verifyDartOutput(
            """
            |/// Generated file. Do not edit.
            |library;
            |
            |/// All supported colors.
            |enum Color {
            |  red,
            |  green
            |}
            |
            |/// Adds a description to a type.
            |mixin Describable {
            |  String describe() {
            |    return 'describable';
            |  }
            |}
            |
            |/// A collection of colors.
            |@deprecated
            |class Palette with Describable {}
            |
            |/// A distance in meters.
            |extension type Meters(double value) {}
            |
            """.trimMargin()
        )
    }
}
