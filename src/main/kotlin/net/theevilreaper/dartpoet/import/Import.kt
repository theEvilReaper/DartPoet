package net.theevilreaper.dartpoet.import

/**
 * The import interface is a marker interface which is used to restrict what an import can be.
 * These implementation of the interface are currently available:
 * - LibraryImports
 *    - library test;
 *    - part of test;
 * - DartImports
 *    - import 'dart:html';
 *    - import 'package 'test.dart';
 *    - import '../model/test.dart';
 * - PartImports
 *    - part 'test.freezed.dart';
 * - TODO: Add export
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
sealed interface Import : Comparable<Import>