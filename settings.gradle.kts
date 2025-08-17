rootProject.name = "DartPoet"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit", "5.13.4")
            version("google.truth", "1.4.4")
            version("kotlin", "2.2.10")
            version("jetbrains.annotations", "26.0.2")
            version("changelog", "2.4.0")
            version("dokka", "2.0.0")

            library("jetbrains.annotations", "org.jetbrains", "annotations").versionRef("jetbrains.annotations")
            library("google.truth", "com.google.truth", "truth").versionRef("google.truth")

            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit.params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")

            plugin("changelog", "org.jetbrains.changelog").versionRef("changelog")
            plugin("dokka", "org.jetbrains.dokka").versionRef("dokka")
            plugin("kotlin.jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")

            bundle("junit", listOf("junit.api", "junit.params"))
        }
    }
}
