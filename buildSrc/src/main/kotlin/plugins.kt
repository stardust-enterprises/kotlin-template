import api.plugin.*

const val KOTLIN_VERSION = "1.6.10"

/**
 * Register your plugins here using the provided DSLs, those try to resemble
 * as good as possible to Gradle's ones.
 *
 * Keeping default plugins as they are is highly recommended as they are used
 * by the boilerplate itself in order for all the automation to work. Modify at
 * your own risk.
 */
fun plugins() {
    // Defaults
    kotlin("jvm")
    kotlin("org.jetbrains.dokka", appendToDefault = false)

    id("org.ajoberstar.grgit") version "4.1.1"
    id("net.kyori.blossom") version "1.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"

    core("maven-publish")
    core("signing")

    // User-defined plugins
    //kotlin("plugin.lombok")
    //id("io.freefair.lombok") version "6.4.1"
}
