plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    id("org.jetbrains.changelog") version "2.0.0"
}

group = "net.theevilreaper.dartpoet"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.truth)
    // testImplementation(libs.kotlin.junit)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            useK2 = false
        }
    }
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

changelog {
    path.set("${project.projectDir}/CHANGELOG.md")
    itemPrefix.set("-")
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
    groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
}