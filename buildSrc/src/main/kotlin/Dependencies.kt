private const val kotlinVersion = "1.6.20"

object Plugins {
    const val KOTLIN = kotlinVersion
    const val GRGIT = "4.1.1" // old version for jgit to work on Java 8
    const val BLOSSOM = "1.3.0"
    const val SHADOW = "7.1.2"
    const val KTLINT = "10.2.1"
    const val DOKKA = "1.6.20"
    const val NEXUS_PUBLISH = "1.0.0"
}

object Dependencies {
    const val KOTLIN = kotlinVersion

    val kotlinModules = arrayOf("stdlib")
}

object Repositories {
    val mavenUrls = arrayOf(
        "https://jitpack.io/",
    )
}
