import api.plugin.*

const val KOTLIN_VERSION = "1.6.10"

fun plugins() {
    kotlin("jvm")
    kotlin("org.jetbrains.dokka", appendToDefault = false)

    id("org.ajoberstar.grgit") version "4.1.1"
    id("net.kyori.blossom") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"

    core("maven-publish")
    core("signing")
}
