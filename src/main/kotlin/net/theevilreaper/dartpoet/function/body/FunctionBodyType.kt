package net.theevilreaper.dartpoet.function.body

enum class FunctionBodyType(val keyword: String) {

    NONE(""),
    ASYNC("async"),
    ASYNC_STAR("async*"),
    SYNC_STAR("sync*");

    val isGenerator: Boolean get() = this == ASYNC_STAR || this == SYNC_STAR

    /** Expected raw return type of generator, or null if none is required. */
    internal val expectedReturnType: String?
        get() = when (this) {
            ASYNC_STAR -> "Stream"
            SYNC_STAR -> "Iterable"
            else -> null
        }
}