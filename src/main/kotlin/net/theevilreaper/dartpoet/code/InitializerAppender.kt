package net.theevilreaper.dartpoet.code

/**
 * An internal interface for appending initializer blocks to a [CodeWriter] for a given type [T].
 * Initializer blocks contain data that should be emitted into a [CodeWriter] instance.
 * This interface provides methods to write initializer blocks for a [CodeBlock] or an object of type [T].
 *
 * @param T the type of object for which initializer blocks are appended.
 * @author theEvilReaper
 * @since 1.0.0
 */
internal interface InitializerAppender<T: Any> {

    /**
     * When a spec object contains data for an initializer block it should be emitted into a writer instance.
     * @param initBlock the [CodeBlock] which contains initializer the data
     * @param writer the [CodeWriter] instance to write the code
     */
    fun writeInitBlock(initBlock: CodeBlock, writer: CodeWriter, isConstantContext: Boolean = true) {
        if (initBlock.isEmpty()) return
        writer.emit("·=·")
        writer.emitCode(initBlock, isConstantContext)
    }

    /**
     * When a spec object contains data for an initializer block it should be emitted into a writer instance.
     * @param spec the spec object which contains the initializer block
     * @param writer the [CodeWriter] instance to write the code
     * @param isConstantContext a flag indicating whether the context is constant (default is true).
     * @throws UnsupportedOperationException if this method is called and not implemented.
     */
    fun writeInitBlock(spec: T, writer: CodeWriter, isConstantContext: Boolean = true) {
        throw UnsupportedOperationException("Not implemented yet")
    }
}
