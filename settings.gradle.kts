rootProject.name = "DartPoet"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("junit", "6.0.2")
            version("google.truth", "1.4.5")
            version("kotlin", "2.3.0")
            version("jetbrains.annotations", "26.0.2-1")
            version("changelog", "2.5.0")
            version("dokka", "2.1.0")
            version("maven.publish", "0.35.0")

            library("jetbrains.annotations", "org.jetbrains", "annotations").versionRef("jetbrains.annotations")
            library("google.truth", "com.google.truth", "truth").versionRef("google.truth")

            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit.params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")

            plugin("changelog", "org.jetbrains.changelog").versionRef("changelog")
            plugin("dokka", "org.jetbrains.dokka").versionRef("dokka")
            plugin("kotlin.jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("maven.publish", "com.vanniktech.maven.publish").versionRef("maven.publish")

            bundle("junit", listOf("junit.api", "junit.params"))
        }
    }
}
