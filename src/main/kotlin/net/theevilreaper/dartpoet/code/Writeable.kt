package net.theevilreaper.dartpoet.code

/**
 * Represents a class that can emit a spec structure to a [CodeWriter].
 * @author theEvilReaper
 * @since 1.0.0
 */
fun interface Writeable<T> {

    /**
     * Emits the content from the generic [T] object to a [CodeWriter] instance.
     * @param spec the spec to emit
     * @param writer the writer to write the spec to
     */
    fun write(spec: T, writer: CodeWriter)
}