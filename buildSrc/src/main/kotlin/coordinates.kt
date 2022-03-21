@file:Suppress("MemberVisibilityCanBePrivate")

import api.coordinate.*

object Coordinates {
    const val name = "kotlin-template"
    const val description = "Project description."
    const val version = "0.0.1"

    const val artifactGroup = "fr.stardustenterprises"
    const val vendor = "Stardust Enterprises"

    const val gitHost = "https://github.com"
    const val repoId = "stardust-enterprises/$name"
    const val gitUrl = "$gitHost/$repoId"
}

object Pom {
    val licenses = arrayOf(
        License("ISC License", "https://opensource.org/licenses/ISC")
    )

    val developers = arrayOf(
        Developer("xtrm", "oss@xtrm.me"),
        Developer("lambdagg", "lambdagg@tuta.io")
    )
}
