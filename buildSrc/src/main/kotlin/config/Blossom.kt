package config

import Coordinates

object Blossom {
    val replacementMap = mutableMapOf<String, String>()

    init {
        // Add your replacements here
    }
}

/*
    mapOf(
        "project.name" to Coordinates.NAME,
        "project.version" to Coordinates.VERSION,
        "project.desc" to Coordinates.DESC,
        "project.rev" to buildRevision,
    ).mapKeys { "@${it.key}@" }.forEach { (key, value) ->
        replaceToken(key, value)
    }
 */
