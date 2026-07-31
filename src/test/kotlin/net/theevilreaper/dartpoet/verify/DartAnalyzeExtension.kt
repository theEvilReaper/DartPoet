package net.theevilreaper.dartpoet.verify

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.InvocationInterceptor
import org.junit.jupiter.api.extension.ReflectiveInvocationContext
import java.lang.reflect.Method

/**
 * JUnit extension backing [DartAnalyzeCase].
 *
 * Executes the intercepted parameterized test invocation and, if it succeeds,
 * records the first resolved test argument in [DartAnalyzeCorpus]. The argument
 * is expected to contain the generated Dart source and is stored using its
 * `toString()` representation.
 *
 * @since 1.2.0
 * @author theEvilReaper
 */
class DartAnalyzeExtension : InvocationInterceptor {

    override fun interceptTestTemplateMethod(
        invocation: InvocationInterceptor.Invocation<Void?>,
        invocationContext: ReflectiveInvocationContext<Method>,
        extensionContext: ExtensionContext
    ) {
        invocation.proceed()
        val generated = invocationContext.arguments.firstOrNull() ?: return
        DartAnalyzeCorpus.record(generated.toString())
    }
}
