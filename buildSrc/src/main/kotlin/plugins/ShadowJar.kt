package plugins

object ShadowJar {
    /**
     * Should the shadow jar override the base jarfile
     * or be its own separate jar.
     */
    const val overrideJar = false

    /**
     * Packages which should be moved
     * to another one.
     *
     * Ex:
     * org.ow2.asm -> me.xtrm.project.libs.asm
     */
    val packageRemappings: Map<String, String> =
        mapOf()
}
