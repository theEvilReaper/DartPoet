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

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            useK2 = true
        }
    }
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}