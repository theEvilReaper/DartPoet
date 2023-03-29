package net.theevilreaper.dartpoet.clazz

import net.theevilreaper.dartpoet.DartModifier

/**
 * The enum contains all class variant which are currently available in dart.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
enum class ClassType(
    internal val keyword: String,
    internal val defaultKeyWords: Set<DartModifier>,
    internal val defaultFunctionModifiers: Set<DartModifier>
) {
    CLASS("class", setOf(DartModifier.PUBLIC), setOf()),
    ABSTRACT("abstract", setOf(DartModifier.ABSTRACT), setOf(DartModifier.ABSTRACT)),
    MIXIN("mixin", setOf(), setOf()),
}