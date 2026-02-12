import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    signing
    `maven-publish`
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
}

group = "net.theevilreaper"
version = "999.0.0-SNAPSHOT"



dependencies {
    compileOnly(libs.jetbrains.annotations)
    testImplementation(libs.google.truth)
    testImplementation(libs.bundles.junit)
    testImplementation(kotlin("test"))
    testRuntimeOnly(libs.junit.engine)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
    }
}

kotlin {
    jvmToolchain(25)
}
mavenPublishing {
    publishToMavenCentral()

    signAllPublications()
    coordinates("net.theevilreaper", "dartpoet", version.toString())


    pom {
        name.set("DartPoet")
        description.set("A Kotlin API which allows the generation of code for dart")
        url.set("https://github.com/theEvilReaper/DartPoet")
        licenses {
            license {
                name.set("AGPL-3.0")
                url.set("https://github.com/theEvilReaper/DartPoet/blob/develop/LICENSE")
            }
        }
        issueManagement {
            system.set("Github")
            url.set("https://github.com/theEvilReaper/DartPoet/issues")
        }
        developers {
            developer {
                id.set("themeinerlp")
                name.set("Phillipp Glanz")
                email.set("p.glanz@madfix.me")
            }
            developer {
                id.set("theEvilReaper")
                name.set("Steffen Wonning")
                email.set("steffenwx@gmail.com")
            }
        }
        scm {
            connection.set("scm:git@github.com:theEvilReaper/DartPoet.git")
            developerConnection.set("scm:git@github.com:theEvilReaper/DartPoet.git")
            url.set("https://github.com/theEvilReaper/DartPoet")
        }
    }
}
