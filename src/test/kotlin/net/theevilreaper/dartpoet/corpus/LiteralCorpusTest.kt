package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.DartModifier
import net.theevilreaper.dartpoet.property.PropertySpec
import net.theevilreaper.dartpoet.type.DOUBLE
import net.theevilreaper.dartpoet.type.INTEGER
import net.theevilreaper.dartpoet.type.STRING
import net.theevilreaper.dartpoet.type.TypeName
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

private fun finalProperty(name: String, type: TypeName, format: String, value: Any?): PropertySpec =
    PropertySpec.builder(name, type)
        .modifier { DartModifier.FINAL }
        .initWith(format, value)
        .build()

@DisplayName("Corpus tests for literal placeholders verified against the Dart analyzer")
class LiteralCorpusTest {

    @Test
    fun `test double literals keep their value`() {
        val file = DartFile.builder("double_literals")
            .properties(
                finalProperty("regular", DOUBLE, "%L", 1.5),
                finalProperty("small", DOUBLE, "%L", 0.00001),
                finalProperty("tiny", DOUBLE, "%L", 1.5e-7),
                finalProperty("negativeZero", DOUBLE, "%L", -0.0),
            )
            .build()

        file.verifyDartOutput(
            """
            |final double regular = 1.5;
            |final double small = 0.00001;
            |final double tiny = 0.00000015;
            |final double negativeZero = -0.0;
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test numeric literals do not use digit separators`() {
        val file = DartFile.builder("numeric_literals_without_separators")
            .properties(
                finalProperty("thousand", INTEGER, "%L", 1000),
                finalProperty("large", INTEGER, "%L", 9_007_199_254_740_991L),
                finalProperty("negative", INTEGER, "%L", -1_234_567),
                finalProperty("fraction", DOUBLE, "%L", 123456.789),
            )
            .build()

        file.verifyDartOutput(
            """
            |final int thousand = 1000;
            |final int large = 9007199254740991;
            |final int negative = -1234567;
            |final double fraction = 123456.789;
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test non finite double literals`() {
        val file = DartFile.builder("non_finite_double_literals")
            .properties(
                finalProperty("notANumber", DOUBLE, "%L", Double.NaN),
                finalProperty("positiveInfinity", DOUBLE, "%L", Double.POSITIVE_INFINITY),
                finalProperty("negativeInfinity", DOUBLE, "%L", Double.NEGATIVE_INFINITY),
            )
            .build()

        file.verifyDartOutput(
            """
            |final double notANumber = double.nan;
            |final double positiveInfinity = double.infinity;
            |final double negativeInfinity = double.negativeInfinity;
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test multiline string literals escape dollar and backslash`() {
        val file = DartFile.builder("multiline_string_literals")
            .property(finalProperty("text", STRING, "%C", "Price: \$5\nPath: C:\\temp"))
            .build()

        file.verifyDartOutput(
            """
            |final String text = '''Price: \${'$'}5
            |Path: C:\\temp''';
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test multiline string literals keep carriage returns`() {
        val file = DartFile.builder("multiline_carriage_return_literals")
            .property(finalProperty("crlf", STRING, "%C", "first\r\nsecond"))
            .build()

        file.verifyDartOutput(
            """
            |final String crlf = '''first\r
            |second''';
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test multiline string literals keep a whitespace only first line`() {
        val file = DartFile.builder("multiline_leading_line_literals")
            .properties(
                finalProperty("leadingNewline", STRING, "%C", "\nfirst\nsecond"),
                finalProperty("leadingSpaces", STRING, "%C", "  \n  indented"),
            )
            .build()

        file.verifyDartOutput(
            """
            |final String leadingNewline = '''\nfirst
            |second''';
            |final String leadingSpaces = '''  \n  indented''';
            |
            """.trimMargin()
        )
    }
}
