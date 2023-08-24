rootProject.name = "DartPoet"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("google.truth", "com.google.truth", "truth").version("1.1.5")
            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").version("5.10.0")
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").version("5.10.0")
            library("junit.params", "org.junit.jupiter", "junit-jupiter-params").version("5.10.0")
        }
    }
}