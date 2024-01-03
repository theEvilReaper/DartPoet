package net.theevilreaper.dartpoet.directive

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ExportDirectiveTest {

    companion object {

        @JvmStatic
        private fun invalidExportDirectives(): Stream<Arguments> = Stream.of(
            Arguments.of(
                { ExportDirective("") },
                "The path of an directive can't be empty"
            ),
            Arguments.of(
                { ExportDirective("dart:math", CastType.DEFERRED, "math") },
                "The following cast types are not allowed for an export directive: [DEFERRED, AS]"
            ),
            Arguments.of(
                { ExportDirective("dart:math", CastType.AS, "math") },
                "The following cast types are not allowed for an export directive: [DEFERRED, AS]"
            )
        )

        @JvmStatic
        private fun exportDirectives(): Stream<Arguments> = Stream.of(
            Arguments.of(ExportDirective("test.dart"), "export 'test.dart';"),
            Arguments.of(ExportDirective("new_lib"), "export 'new_lib.dart';"),
            Arguments.of(ExportDirective("new_lib", CastType.SHOW, "lib"), "export 'new_lib.dart' show lib;"),
            Arguments.of(ExportDirective("new_lib", CastType.HIDE, "lib"), "export 'new_lib.dart' hide lib;")
        )
    }

    @ParameterizedTest
    @MethodSource("invalidExportDirectives")
    fun `test invalid export directives`(current: () -> ExportDirective, expected: String) {
        val exception = assertThrows<IllegalStateException> { current() }
        assertEquals(IllegalStateException::class.java, exception.javaClass)
        assertEquals(expected, exception.message)
    }

    @ParameterizedTest
    @MethodSource("exportDirectives")
    fun `test export directives`(current: ExportDirective, expected: String) {
        assertEquals(expected, current.asString())
    }
}
