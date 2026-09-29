package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for the name placeholder verified against the Dart analyzer")
class IdentifierCorpusTest {

    @Test
    fun `test built-in and contextual keywords as variable names`() {
        // Built-in identifiers, contextual and unrestricted keywords are valid names outside of type declarations
        val names = listOf("on", "get", "set", "when", "required", "late", "async", "await", "type", "sealed")
        val declarations = names.indices.map { index -> "final %N = $index;" }
        val usage = "print([${names.joinToString(", ") { "%N" }}]);"

        val file = DartFile.builder("keyword_identifiers")
            .function(
                FunctionSpec.builder("identifiers")
                    .addCode((declarations + usage).joinToString("\n"), *(names + names).toTypedArray())
                    .build()
            )
            .build()

        file.verifyDartOutput(
            """
            |void identifiers() {
            |  final on = 0;
            |  final get = 1;
            |  final set = 2;
            |  final when = 3;
            |  final required = 4;
            |  final late = 5;
            |  final async = 6;
            |  final await = 7;
            |  final type = 8;
            |  final sealed = 9;
            |  print([on, get, set, when, required, late, async, await, type, sealed]);
            |}
            |
            """.trimMargin()
        )
    }
}
