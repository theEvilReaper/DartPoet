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
 * the analyzer corpus.
 */
internal fun Any.verifyDartOutput(expected: String) {
    val actual = toString()
    assertThat(actual).isEqualTo(expected)
    DartAnalyzeCorpus.record(actual)
}
