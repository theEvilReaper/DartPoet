package net.theevilreaper.dartpoet.verify

import com.google.common.truth.Truth.assertThat
import java.util.Collections

/**
 * Collects a generated Dart source during a test run for later analysis with the Dart toolchain.
 *
 * Tests opt in by using [verifyDartOutput]. Once the test plan finishes,
 * [DartAnalyzeCorpusListener] writes the collected sources to
 * `build/dart-analyze-corpus`, where they can be validated by the
 * `dartAnalyzeCorpus` Gradle task.
 *
 * @since 1.2.0
 * @author theEvilReaper
 */
internal object DartAnalyzeCorpus {

    private val recorded = Collections.synchronizedList(mutableListOf<String>())

    fun record(dartSource: String) {
        recorded += dartSource
    }

    fun snapshot(): List<String> = recorded.toList()
}

/**
 * Verifies that this object's generated Dart source matches [expected] and,
 * if the assertion succeeds, records the output in [DartAnalyzeCorpus].
 *
 * Prefer this helper over a plain `assertThat(...).isEqualTo(...)` whenever
 * the generated output represents valid Dart code that should be included in
 * the analyzer corpus. The blank lines are checked against the Dart style as well.
 */
internal fun Any.verifyDartOutput(expected: String) {
    val actual = toString()
    assertThat(actual).isEqualTo(expected)
    assertBlankLineStyle()
    DartAnalyzeCorpus.record(actual)
}

/**
 * The placeholder in a context of [verifyDartOutputIn] which is replaced with the generated source.
 */
internal const val GENERATED_SOURCE = "%GENERATED%"

/**
 * Verifies the generated Dart source like [verifyDartOutput], but for declarations which are only valid
 * inside an enclosing declaration, such as constructors. The generated source replaces [GENERATED_SOURCE]
 * in [context] before the result is recorded in [DartAnalyzeCorpus].
 */
internal fun Any.verifyDartOutputIn(context: String, expected: String) {
    require(context.contains(GENERATED_SOURCE)) { "The context must contain $GENERATED_SOURCE" }
    val actual = toString()
    assertThat(actual).isEqualTo(expected)
    assertBlankLineStyle()
    DartAnalyzeCorpus.record(context.replace(GENERATED_SOURCE, actual.prependIndent("  ")))
}
