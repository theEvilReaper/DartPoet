rootProject.name = "DartPoet"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit", "5.10.1")
            library("google.truth", "com.google.truth", "truth").version("1.2.0")
            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit.params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")

            plugin("changelog", "org.jetbrains.changelog").version("2.2.0")
            plugin("dokka", "org.jetbrains.dokka").version("1.9.10")
            plugin("kotlin.jvm", "org.jetbrains.kotlin.jvm").version("1.9.22")
        }
    }
}
