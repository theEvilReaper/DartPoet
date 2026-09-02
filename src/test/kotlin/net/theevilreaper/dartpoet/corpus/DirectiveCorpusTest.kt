package net.theevilreaper.dartpoet.corpus

import net.theevilreaper.dartpoet.DartFile
import net.theevilreaper.dartpoet.directive.DirectiveFactory
import net.theevilreaper.dartpoet.directive.DirectiveType
import net.theevilreaper.dartpoet.function.FunctionSpec
import net.theevilreaper.dartpoet.verify.verifyDartOutput
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Corpus tests for directives ordering verified against the Dart analyzer")
class DirectiveCorpusTest {

    @Test
    fun `test directive ordering matches Effective Dart guidelines`() {
        val file = DartFile.builder("effective_dart_directives")
            .directive(DirectiveFactory.createLib("effective_dart_directives"))
            .directive(DirectiveFactory.create(DirectiveType.IMPORT, "dart:async"))
            .directive(DirectiveFactory.create(DirectiveType.IMPORT, "dart:math"))
            .directive(DirectiveFactory.create(DirectiveType.IMPORT, "flutter/material.dart"))
            .directive(DirectiveFactory.create(DirectiveType.IMPORT, "path/path.dart"))
            .directive(DirectiveFactory.create(DirectiveType.RELATIVE, "../models/user.dart"))
            .directive(DirectiveFactory.createPackage("local_helper"))
            .directive(DirectiveFactory.create(DirectiveType.EXPORT, "src/export_me.dart"))
            .directive(DirectiveFactory.create(DirectiveType.PART, "effective_dart_directives.g.dart"))
            .function(
                FunctionSpec.builder("main")
                    .addCode("print('ok');")
                    .build()
            )
            .build()

        file.verifyDartOutput(
            """
            |library effective_dart_directives;
            |
            |import 'dart:async';
            |import 'dart:math';
            |
            |import 'package:flutter/material.dart';
            |import 'package:path/path.dart';
            |
            |import '../models/user.dart';
            |import 'local_helper.dart';
            |
            |export 'src/export_me.dart';
            |
            |part 'effective_dart_directives.g.dart';
            |
            |void main() {
            |  print('ok');
            |}
            |
            """.trimMargin()
        )
    }

    @Test
    fun `test file with only package directive`() {
        val file = DartFile.builder("local_import_file")
            .directive(DirectiveFactory.createPackage("local_file"))
            .function(
                FunctionSpec.builder("run")
                    .addCode("print('running');")
                    .build()
            )
            .build()

        file.verifyDartOutput(
            """
            |import 'local_file.dart';
            |
            |void run() {
            |  print('running');
            |}
            |
            """.trimMargin()
        )
    }
}
