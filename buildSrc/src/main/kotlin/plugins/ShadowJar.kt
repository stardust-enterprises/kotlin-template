package plugins

object ShadowJar {
    /**
     * The shadow jar file classifier.
     *
     * Put blank if you want to override the default output jar.
     */
    const val classifier = "all"

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
