private const val kotlinVersion = "1.6.10"

object Plugins {
    const val KOTLIN = kotlinVersion
    const val DOKKA = kotlinVersion
    const val NEXUS_PUBLISH = "1.0.0"
    const val KTLINT = "10.2.1"
    const val BLOSSOM = "1.3.0"
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
