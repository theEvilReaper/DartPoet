import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    signing
    `maven-publish`
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka)
    id("com.vanniktech.maven.publish") version "0.35.0"
}

group = "net.theevilreaper"
version = System.getenv("TAG_VERSION") ?: "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

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
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}

kotlin {
    jvmToolchain(21)
}

changelog {
    path.set("${project.projectDir}/CHANGELOG.md")
    itemPrefix.set("-")
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
    groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
}

//nmcpAggregation {
//    centralPortal {
//        username = System.getenv("OSSRH_USERNAME")
//        password = System.getenv("OSSRH_PASSWORD")
//        publishingType = "MANUAL"
//    }
//
//    // Publish all projects that apply the 'maven-publish' plugin
//    publishAllProjectsProbablyBreakingProjectIsolation()
//}
mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    signAllPublications()
    coordinates(group.toString(), "dartpoet", version.toString())


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
