plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.changeLog)
    `maven-publish`
    signing
    alias(libs.plugins.dokka)
    `java-library`
}

group = "net.theevilreaper.dartpoet"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.truth)
    testImplementation(libs.junitApi)
    testImplementation(libs.junitParams)
    testRuntimeOnly(libs.junitEngine)
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
            //languageVersion = "2.0"

        }
    }

}

val sourceJar by tasks.register<Jar>("kotlinJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(rootProject.tasks.dokkaHtml)
    from(rootProject.tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
}

val dokkaHtmlJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(rootProject.tasks.dokkaJavadoc)
    from(rootProject.tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
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
            from(components.findByName("java"))
            groupId = "dev.themeinerlp"
            artifactId = "dartpoet"
            version = rootProject.version.toString()
            artifact(dokkaJavadocJar)
            artifact(dokkaHtmlJar)
            artifact(sourceJar)
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
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = if (version.toString().endsWith("SNAPSHOT")) uri(snapshotsRepoUrl) else uri(releasesRepoUrl)
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}
