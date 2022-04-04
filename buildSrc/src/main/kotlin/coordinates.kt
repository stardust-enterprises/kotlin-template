@file:Suppress("MemberVisibilityCanBePrivate")

import api.coordinate.*

/**
 * Coordinates of this artifact.
 */
object Coordinates {
    const val name = "ktemplate"
    const val description = "Project description."
    const val version = "0.0.1"

    const val artifactGroup = "fr.stardustenterprises"
    const val vendor = "Stardust Enterprises"

    const val gitHost = "github.com"
    const val repoId = "stardust-enterprises/$name"
    const val gitUrl = "https://$gitHost/$repoId"
}

/**
 * Extra POM data of this artifact.
 */
object Pom {
    val licenses = arrayOf(
        License("ISC License", "https://opensource.org/licenses/ISC")
    )

    val developers = arrayOf(
        Developer("xtrm", "oss@xtrm.me"),
        Developer("lambdagg", "lambdagg@tuta.io"),
        Developer("Shyrogan", "<none>")
    )
}
