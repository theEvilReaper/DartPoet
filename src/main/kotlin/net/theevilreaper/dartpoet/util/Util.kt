package net.theevilreaper.dartpoet.util

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

internal fun <T> Collection<T>.toImmutableSet(): Set<T> =
    Collections.unmodifiableSet(LinkedHashSet(this))

internal fun <T> Collection<T>.toImmutableList(): List<T> =
    Collections.unmodifiableList(ArrayList(this))
