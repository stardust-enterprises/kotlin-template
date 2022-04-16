fun main() {
    val testImpl = ITest { "Hello, world!" }
    with(testImpl) { printTest() }

    val projectName = "@project.name@"
    val projectVersion = "@project.version@"
    println("Project: $projectName v$projectVersion")
    println("Description: @project.desc@")
}

context(ITest) fun printTest() {
    println(string())
}
