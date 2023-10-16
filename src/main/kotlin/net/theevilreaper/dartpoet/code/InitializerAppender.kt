package net.theevilreaper.dartpoet.code

/**
 *
 * @author theEvilReaper
 * @since 1.0.0
 */
internal interface InitializerAppender {

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
}