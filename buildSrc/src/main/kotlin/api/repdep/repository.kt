package api.repdep

import repositories

import java.net.URL
import java.nio.file.Path
import java.util.*

internal val REPOSITORY_REGISTRY = mutableListOf<Repository>()

val REPOSITORIES: List<Repository> by lazy {
    repositories()
    REPOSITORY_REGISTRY
}

fun maven(url: String, vararg artifactUrls: String) =
    Repository(
        arrayOf(URL("https://$url")),
        artifactUrls.map { URL(it) }.toTypedArray(),
    )

fun ivy(url: String, layout: IvyLayout = IvyLayout.GRADLE, vararg artifactUrls: String) =
    Repository(
        arrayOf(URL("https://$url")),
        artifactUrls.map { URL(it) }.toTypedArray(),
        layout,
    )

fun flatDir(vararg path: Path) =
    Repository(path.map { it.toUri().toURL() }.toTypedArray())

data class Repository(
    val url: Array<URL>,
    val artifactsUrl: Array<URL> = arrayOf(),
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

        return url.contentEquals((other as Repository).url)
    }

    override fun hashCode(): Int = url.contentHashCode()
}

enum class IvyLayout {
    GRADLE, MAVEN, IVY;

    override fun toString(): String =
        name.toLowerCase(Locale.getDefault())
}
