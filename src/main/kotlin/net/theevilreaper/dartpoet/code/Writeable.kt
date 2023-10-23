package net.theevilreaper.dartpoet.code

/**
 * Represents a class that can emit a spec structure to a [CodeWriter].
 * @author theEvilReaper
 * @since 1.0.0
 */
interface Writeable<T> {

    /**
     * Emits the given [spec] to the [CodeWriter].
     * @param spec the spec to emit
     * @param writer the writer to write the spec to
     */
    fun write(spec: T, writer: CodeWriter)
}