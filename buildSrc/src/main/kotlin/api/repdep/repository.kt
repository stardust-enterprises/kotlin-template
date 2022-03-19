package api.repdep

import java.net.URL

val REPOSITORIES = mutableListOf<Dependency>()

fun maven(url: String) =
    Repository(URL(url))

data class Repository(
    val url: URL,
)
