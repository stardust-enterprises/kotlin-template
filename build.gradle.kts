plugins {
    `java-library`
    kotlin("jvm") version Plugins.KOTLIN
    id("org.jetbrains.dokka") version Plugins.DOKKA
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version Plugins.NEXUS_PUBLISH
    id("org.jlleitschuh.gradle.ktlint") version Plugins.KTLINT
}

val jvmTarget = "1.8"
addApiSourceSet()

group = Coordinates.GROUP
version = Coordinates.VERSION

repositories {
    mavenCentral()
}

dependencies {
    Dependencies.kotlinModules.forEach {
        implementation("org.jetbrains.kotlin", "kotlin-$it", Plugins.KOTLIN)
    }

    testImplementation("org.jetbrains.kotlin", "kotlin-test", Plugins.KOTLIN)
}

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
    compileJava {
        targetCompatibility = jvmTarget
        sourceCompatibility = jvmTarget
    }
}

addDefaultArtifacts()
setupMavenPublications()

nexusPublishing.repositories.sonatype {
    nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
    snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

    // Skip this step if environment variables NEXUS_USERNAME or NEXUS_PASSWORD aren't set.
    username.set(properties["NEXUS_USERNAME"] as? String ?: return@sonatype)
    password.set(properties["NEXUS_PASSWORD"] as? String ?: return@sonatype)
}
