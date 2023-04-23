plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    id("org.jetbrains.changelog") version "2.0.0"
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.8.10"
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
            languageVersion = "2.0"
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
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components.findByName("kotlin"))
            groupId = "dev.themeinerlp"
            artifactId = "dartPoet"
            version = rootProject.version.toString()

            pom {
                name.set("Dart Poet")
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
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = if (version.toString().endsWith("SNAPSHOT")) uri(snapshotsRepoUrl) else uri(releasesRepoUrl)
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_TOKEN")
            }
        }
    }
}
signing {
    useInMemoryPgpKeys(System.getenv("SIGNING_KEY"), System.getenv("SIGNING_PASSWORD"))
    sign(publishing.publications)
}