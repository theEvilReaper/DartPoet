package net.theevilreaper.dartpoet.verify

import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestPlan
import java.io.File

/**
 * JUnit Platform listener that persists the contents of [DartAnalyzeCorpus] as
 * individual `.dart` files in `build/dart-analyze-corpus` after the test plan
 * has finished.
 *
 * The listener is discovered automatically via the JUnit Platform `ServiceLoader`
 * mechanism and runs as part of every test execution. If no test contributed
 * Dart sources, the output directory is simply left empty.
 *
 * The generated files are analyzed by the dedicated `dartAnalyze` task. Analyzer
 * configuration is picked up automatically from the project's
 * `analysis_options.yaml`.
 *
 * @since 1.2.0
 * @author theEvilReaper
 */
class DartAnalyzeCorpusListener : TestExecutionListener {

    override fun testPlanExecutionFinished(testPlan: TestPlan) {
        val outputDir = File("build/dart-analyze-corpus")
        outputDir.deleteRecursively()
        outputDir.mkdirs()

        DartAnalyzeCorpus.snapshot().forEachIndexed { index, dartSource ->
            File(outputDir, "case_$index.dart").writeText(dartSource)
        }
    }
}
