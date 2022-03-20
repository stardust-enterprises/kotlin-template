package api.plugin

import KOTLIN_VERSION
import plugins

/**
 * The string to attribute to a Gradle plugin that should not embed any version,
 * AKA core plugins such as `signing` and `maven-publish`.
 */
const val CORE_VERSION = "@core"

internal val PLUGIN_REGISTRY = mutableListOf<Plugin>()

/**
 * A list of plugins loaded from the `plugins.kt` file.
 */
val PLUGINS: List<Plugin> by lazy {
    plugins()
    PLUGIN_REGISTRY
}

fun id(id: String): Plugin = Plugin(id).apply { PLUGIN_REGISTRY.add(this) }

fun kotlin(id: String, appendToDefault: Boolean = true): Plugin =
    id(
        (if (appendToDefault) "org.jetbrains.kotlin." else "") + id
    ) version KOTLIN_VERSION

fun core(id: String): Plugin =
    id(id) version CORE_VERSION

infix fun Plugin.version(version: String): Plugin =
    this.apply {
        this.version = version
    }

infix fun Plugin.apply(apply: Boolean): Plugin =
    this.apply {
        this.defaultApply = apply
    }

data class Plugin(
    var id: String = "",
    var version: String = "+",
    var defaultApply: Boolean = true,
)
