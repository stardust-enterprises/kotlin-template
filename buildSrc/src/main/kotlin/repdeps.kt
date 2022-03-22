import api.repdep.*

/**
 * Register your repositories here using the provided DSL, those try to
 * resemble as good as possible to Gradle's ones.
 */
fun repositories() {
    //maven("jitpack.io")
}

/**
 * Register your dependencies here using the provided DSLs, those try to
 * resemble as good as possible to Gradle's ones.
 * To use a custom config, you can call the [withConfig] DSL function.
 */
fun dependencies() {
    // Defaults
    implementation(kotlin("stdlib"))

    // User-defined dependencies
    //testImplementation(kotlin("test"))
}
