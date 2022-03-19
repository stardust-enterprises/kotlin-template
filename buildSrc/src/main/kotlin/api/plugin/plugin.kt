package api.plugin

import KOTLIN_VERSION
import plugins

val CORE_VERSION = "@core"

internal val PLUGIN_REGISTRY = mutableListOf<Plugin>()

val PLUGINS: List<Plugin> by lazy {
    plugins()
    PLUGIN_REGISTRY
}

data class Plugin(
    var id: String = "",
    var version: String = "+",
    var defaultApply: Boolean = true,
)

fun id(id: String): Plugin = run {
    val plugin = Plugin(id)
    PLUGIN_REGISTRY.add(plugin)
    plugin
}

fun kotlin(id: String, appendToDefault: Boolean = true): Plugin =
    id(
        (if (appendToDefault) "org.jetbrains.kotlin." else "") + id
    ) version KOTLIN_VERSION

fun core(id: String): Plugin =
    id(id) version CORE_VERSION

infix fun Plugin.version(version: String): Plugin = apply {
    this.version = version
}

infix fun Plugin.apply(apply: Boolean): Plugin = apply {
    this.defaultApply = apply
}
