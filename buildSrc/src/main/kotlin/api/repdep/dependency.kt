package api.repdep

import KOTLIN_VERSION
import dependencies

const val UNDEFINED_CONFIG = "_undefined"

internal val DEPENDENCY_REGISTRY = mutableListOf<Dependency>()

val DEPENDENCIES: List<Dependency> by lazy {
    dependencies()
    DEPENDENCY_REGISTRY
}

fun withConfig(
    config: String,
    group: String,
    name: String,
    version: String = "+"
): Dependency {
    val dep = Dependency(group, name, version, config)
    DEPENDENCY_REGISTRY.add(dep)
    return dep
}

fun kotlin(name: String) =
    Dependency(
        group = "org.jetbrains.kotlin",
        name = "kotlin-$name",
        version = KOTLIN_VERSION
    )

fun testImplementation(dep: Dependency) =
    withConfig("testImplementation", dep.group, dep.name, dep.version)

fun testImplementation(
    group: String,
    name: String,
    version: String = "+"
) = testImplementation(Dependency(group, name, version))

fun implementation(dep: Dependency) =
    withConfig("implementation", dep.group, dep.name, dep.version)

fun implementation(group: String, name: String, version: String = "+") =
    implementation(Dependency(group, name, version))

fun include(dep: Dependency) =
    withConfig("include", dep.group, dep.name, dep.version)

data class Dependency(
    var group: String,
    var name: String,
    var version: String = "+",
    var config: String = UNDEFINED_CONFIG,
)
