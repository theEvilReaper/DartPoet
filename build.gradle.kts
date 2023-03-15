plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "net.theevilreaper.dartpoet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.truth)
    testImplementation(libs.kotlin.junit)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}