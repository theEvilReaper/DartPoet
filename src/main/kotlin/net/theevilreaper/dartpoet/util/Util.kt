package net.theevilreaper.dartpoet.util

import java.util.*
import kotlin.collections.LinkedHashSet

internal fun <T> Collection<T>.toImmutableSet(): Set<T> =
    Collections.unmodifiableSet(LinkedHashSet(this))
