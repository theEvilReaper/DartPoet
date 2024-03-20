import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    signing
    `maven-publish`
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka)
}

group = "net.theevilreaper.dartpoet"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    testImplementation(kotlin("test"))
    testImplementation(libs.google.truth)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
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
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

val sourceJar by tasks.register<Jar>("kotlinJar") {
    description = "Creates a JAR archive containing all source files"
    group = JavaBasePlugin.BUILD_TASK_NAME
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaHtmlJar") {
    description = "Generates the documentation in the HTML format"
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    dependsOn(rootProject.tasks.dokkaHtml)
    from(rootProject.tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
}

val dokkaHtmlJar by tasks.register<Jar>("dokkaJavadocJar") {
    description = "Generates documentation in Javadoc format"
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    dependsOn(rootProject.tasks.dokkaJavadoc)
    from(rootProject.tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

kotlin {
    jvmToolchain(17)
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
