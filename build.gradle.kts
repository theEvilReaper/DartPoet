import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    signing
    `maven-publish`
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
}

version = "1.0.15" //x-release-please-version

dependencies {
    compileOnly(libs.jetbrains.annotations)
    testImplementation(libs.google.truth)
    testImplementation(libs.bundles.junit)
    testImplementation(libs.junit.platform.launcher)
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

    /**
     * Runs `dart analyze` over the generated corpus in `build/dart-analyze-corpus`.
     *
     * The corpus is collected from calls to `verifyDartOutput(expected)` during the
     * test run (see `DartAnalyzeCorpus.kt`).
     *
     * This task is intentionally kept separate from `test`, `build`, and `check`
     * because it requires a Dart SDK to be available on `PATH`. CI executes it
     * explicitly after installing the Dart SDK (see `.github/workflows/dart-analyze.yml`).
     */
    register<Exec>("dartAnalyzeCorpus") {
        description = "Runs `dart analyze` over the generated corpus in `build/dart-analyze-corpus`"
        dependsOn(test)
        commandLine("dart", "analyze", "build/dart-analyze-corpus")
    }
}

kotlin {
    jvmToolchain(25)
}
publishing {
    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    // Those credentials need to be set under "Settings -> Secrets -> Actions" in your repository
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            }
            name = "OneLiteFeatherRepository"
            url = if (rootProject.version.toString().contains("SNAPSHOT")) {
                uri("https://repo.onelitefeather.dev/onelitefeather-snapshots")
            } else {
                uri("https://repo.onelitefeather.dev/onelitefeather-releases")
            }
        }
    }
}
mavenPublishing {

    signAllPublications()
    coordinates("net.theevilreaper", "dartpoet", rootProject.version.toString())


    pom {
        name.set("DartPoet")
        description.set("A Kotlin API which allows the generation of code for dart")
        url.set("https://github.com/theEvilReaper/DartPoet")
        licenses {
            license {
                name.set("AGPL-3.0")
                url.set("https://github.com/theEvilReaper/DartPoet/blob/main/LICENSE")
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
