package net.theevilreaper.dartpoet.import

/**
 * Represent all available import casts from flutter.
 * @author theEvilReaper
 * @since 1.0.0
 */
enum class ImportCastType(
    val identifier: String
){
    AS("as"),
    SHOW("show"),
    HIDE("hide"),
    DEFERRED("deferred as")
}
