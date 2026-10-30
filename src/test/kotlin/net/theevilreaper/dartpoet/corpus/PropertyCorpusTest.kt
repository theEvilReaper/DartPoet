package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.clazz.ClassSpec
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for PropertySpec verified against the Dart analyzer")
class PropertyCorpusTest {

    @Test
    fun `test private const properties with and without type`() {
        val file = DartFile.builder("private_constants")
            .type(
                ClassSpec.builder("Limits")
                    .properties(
                        PropertySpec.constBuilder("maxItems")
                            .modifier(DartModifier.PRIVATE)
                            .initWith("%L", 10)
                            .build(),
                        PropertySpec.constBuilder("minItems", INTEGER)
                            .modifier(DartModifier.PRIVATE)
                            .initWith("%L", 1)
                            .build()
                    )
                    .function(
                        FunctionSpec.builder("range")
                            .modifier(DartModifier.STATIC)
                            .returns(INTEGER)
                            .addCode("return _maxItems - _minItems;")
                            .build()
                    )
                    .build()
            )
            .build()

        file.verifyDartOutput(
            """
            |class Limits {
            |  static const _maxItems = 10;
            |  static const int _minItems = 1;
            |
            |  static int range() {
            |    return _maxItems - _minItems;
            |  }
            |}
            |
            """.trimMargin()
        )
    }
}
