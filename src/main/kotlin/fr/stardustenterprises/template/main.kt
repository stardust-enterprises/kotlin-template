package fr.stardustenterprises.template

fun main() {
    val projectName = "@coordinates.name@"
    val projectVersion = "@coordinates.version@"
    println("Project: $projectName v$projectVersion")
    println("Description: @coordinates.desc@")
}
