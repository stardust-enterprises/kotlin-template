package config

/**
 * Shadow configuration.
 */
object ShadowJar {
    /**
     * Should the shadow jar override the base jar file
     * or be its own separate jar.
     *
     * Default: false
     */
    const val overrideJar = false

    /**
     * Packages which should be moved to (or 'remapped as') another one.
     *
     * Default: (empty)
     */
    val packageRemappings: Map<String, String> = mapOf(
        // "org.ow2.asm" to "me.xtrm.project.libs.asm",
    )
}
