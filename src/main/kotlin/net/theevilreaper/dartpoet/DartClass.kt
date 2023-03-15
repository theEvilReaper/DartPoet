package net.theevilreaper.dartpoet

import net.theevilreaper.dartpoet.import.DartImport
import net.theevilreaper.dartpoet.writer.CodeWriter

class DartClass(
    val builder: Builder
) {

    internal fun emit(
        codeWriter: CodeWriter
    ) {
    }


    class Builder internal constructor(
        val className: String,
        val classType: DartClassType
    ) {

        internal var imports: MutableList<DartImport> = mutableListOf()

        fun addImport(dartImport: DartImport): Builder {
            this.imports += dartImport
            return this
        }

        fun addImports(dartImports: Iterable<DartImport>): Builder {
            this.imports += dartImports
            return this
        }

        fun addImports(dartImport: () -> Iterable<DartImport>): Builder {
            this.imports += dartImport()
            return this
        }

        fun build(): DartClass {
            require(className.trim().isEmpty()) { "The class name can't be empty" }

            return DartClass(this)
        }
    }

    companion object {

        @JvmStatic fun classBuilder(className: String) = Builder(className, DartClassType.CLASS)
    }


}