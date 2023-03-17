package net.theevilreaper.dartpoet.method

class DartMethodSpec(
    builder: DartMethodBuilder
) {

    companion object {

        @JvmStatic fun builder(name: String) = DartMethodBuilder(name)

        @JvmStatic fun constructor(name: String, const: Boolean) {

        }

        @JvmStatic fun namedConstructor(name: String) {

        }
    }
}