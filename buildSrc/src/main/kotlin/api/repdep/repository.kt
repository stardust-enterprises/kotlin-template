package api.repdep

import repositories
import java.net.URI

import java.nio.file.Path
import java.util.*

internal val REPOSITORY_REGISTRY = mutableListOf<Repository>()

/**
 * A list of repositories loaded from the `repdeps.kt` file.
 */
val REPOSITORIES: List<Repository> by lazy {
    repositories()
    REPOSITORY_REGISTRY
}

fun maven(url: String, vararg artifactUrls: String) =
    Repository(
        arrayOf(URI("https://$url")),
        artifactUrls.map { URI(it) }.toTypedArray(),
    )

fun ivy(url: String, layout: IvyLayout = IvyLayout.GRADLE, vararg artifactUrls: String) =
    Repository(
        arrayOf(URI("https://$url")),
        artifactUrls.map { URI(it) }.toTypedArray(),
        layout,
    )

fun flatDir(vararg path: Path) =
    Repository(path.map { it.toUri() }.toTypedArray())

data class Repository(
    val urls: Array<URI>,
    val artifactUrls: Array<URI> = arrayOf(),
    val layout: IvyLayout? = null,
) {
    init { REPOSITORY_REGISTRY.add(this) }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (this.javaClass != other?.javaClass) {
            return false
        }

        return urls.contentEquals((other as Repository).urls)
    }

    override fun hashCode(): Int = urls.contentHashCode()
}

@Suppress("unused")
enum class IvyLayout {
    GRADLE, MAVEN, IVY;

    override fun toString(): String =
        name.toLowerCase(Locale.getDefault())
}
