package net.theevilreaper.dartpoet.code

/**
 * This interface contains a default implementation to append a documentation from a spec to a [CodeWriter] instance.
 * @author theEvilReaper
 * @since 1.0.0
 */
internal interface DocumentationAppender {

    /**
     * When a spec contains any kind of documentation this method will emit it into a [CodeWriter].
     * @param docs the list of documentation
     * @param writer the [CodeWriter] instance to write the code
     */
    fun emitDocumentation(docs: List<CodeBlock>, writer: CodeWriter) {
        if (docs.isEmpty()) return
        docs.forEach { writer.emitDoc(it) }
    }
}