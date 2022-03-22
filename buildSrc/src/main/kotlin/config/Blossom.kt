@file:Suppress("MayBeConstant")

package config

import api.config.BLOSSOM_FORMAT_STRING

/**
 * Blossom configuration.
 */
object Blossom {
    /**
     * Replacements themselves go here, meaning everything that matches using
     * the [replacementInputFormat] will be replaced accordingly to the
     * [replacementOutputFormat].
     *
     * There are default replacements, matching the properties of the
     * [Coordinates] object, such as in the default `src/main/kotlin/Test.kt`.
     *
     * Default: (empty)
     */
    val replacementMap: Map<String, String> = mapOf(
        // "apple" to "banana",
    )

    /**
     * The input format of the Blossom replacement.
     *
     * Default: "@$BLOSSOM_FORMAT_STRING@"
     */
    val replacementInputFormat: String = "@$BLOSSOM_FORMAT_STRING@"

    /**
     * The output format of the Blossom replacement.
     * If empty, will just replace the input matching the format with the
     * direct output. However, if set to anything else, will use the
     * [BLOSSOM_FORMAT_STRING] to match a string and replace it with the wanted
     * one.
     *
     * Default: ""
     */
    val replacementOutputFormat: String = ""
}
