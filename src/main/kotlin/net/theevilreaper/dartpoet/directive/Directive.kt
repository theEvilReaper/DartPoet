package net.theevilreaper.dartpoet.directive

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
 *
 * @since 1.0.0
 * @author theEvilReaper
 */
sealed interface Directive : Comparable<Directive>