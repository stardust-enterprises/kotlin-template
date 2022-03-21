package api.config

import java.util.UUID

/**
 * The text to replace with the matching stuff in the Blossom config.
 *
 * @see config.Blossom
 */
val BLOSSOM_FORMAT_STRING = "{__match__${UUID.randomUUID()}__}"
