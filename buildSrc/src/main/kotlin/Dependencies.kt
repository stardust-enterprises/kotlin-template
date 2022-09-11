private const val kotlinVersion = "1.7.10"

object Plugins {
    const val KOTLIN = kotlinVersion
    const val GRGIT = "4.1.1" // old version for jgit to work on Java 8
    const val BLOSSOM = "1.3.1"
    const val SHADOW = "7.1.2"
    const val KTLINT = "11.0.0"
    const val DOKKA = "1.7.10"
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
