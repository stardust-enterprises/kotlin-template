fun main() {
    val testImpl = ITest { "Hello, world!" }
    println(testImpl.string())

    val projectName = "@project.name@"
    val projectVersion = "@project.version@"
    println("Project: $projectName v$projectVersion")
    println("Description: @project.desc@")
}
