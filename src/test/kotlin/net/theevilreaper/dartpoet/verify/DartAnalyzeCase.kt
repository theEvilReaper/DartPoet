package net.theevilreaper.dartpoet.verify

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Marks a `@ParameterizedTest` whose first argument contains generated Dart
 * output that should be verified by a real Dart toolchain.
 *
 * After a successful test invocation, [DartAnalyzeExtension] records the first
 * argument's `toString()` representation in [DartAnalyzeCorpus].
 *
 * This annotation only applies to `@ParameterizedTest` methods, as the generated
 * output must be provided as a method argument. For regular `@Test` methods, use
 * [verifyDartOutput] instead.
 *
 * @since 1.2.0
 * @author theEvilReaper
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Tag("dart-analyze")
@ExtendWith(DartAnalyzeExtension::class)
annotation class DartAnalyzeCase
