package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier

/**
 * The [ClassType] enum defines all class variants which are currently available in the programming language Dart.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
enum class ClassType(
    internal val keyword: String,
) {
    CLASS(DartModifier.CLASS.identifier),
    ABSTRACT(DartModifier.ABSTRACT.identifier),
    MIXIN(DartModifier.MIXIN.identifier),
    ENUM(DartModifier.ENUM.identifier),
    LIBRARY(DartModifier.LIBRARY.identifier)
}