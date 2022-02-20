
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named

var apiExists = false

private fun Project.sourceSets(action: SourceSetContainer.() -> Unit) =
    (this as ExtensionAware).extensions.configure("sourceSets", action)

private val Project.sourceSets: SourceSetContainer
    get() =
        (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

fun Project.addApiSourceSet() {
    sourceSets {
        val name = "api"

        val main = getByName("main")
        val test = getByName("test")

        val sourceSet = create(name) {
            java {
                srcDir("src/$name/kotlin")
                resources.srcDir("src/$name/resources")
            }

            this.compileClasspath += main.compileClasspath
            this.runtimeClasspath += main.runtimeClasspath
        }

        listOf(main, test).forEach {
            it.compileClasspath += sourceSet.output
            it.runtimeClasspath += sourceSet.output
        }

        apiExists = true
    }
}

val defaultArtifactTasks: MutableList<Task>
    get() = mutableListOf()

fun Project.addDefaultArtifacts() {
    val block: TaskContainer.() -> Unit = {
        // The original artifact, we just have to add the API source output and the
        // LICENSE file.
        named<org.gradle.api.tasks.bundling.Jar>("jar") {
            if (apiExists) {
                from(sourceSets["api"].output)
            }
            from("LICENSE")
        }

        if (apiExists) {
            // API artifact, only including the output of the API source and the
            // LICENSE file.
            defaultArtifactTasks += create("apiJar", Jar::class) {
                group = "build"

                archiveClassifier.set("api")
                from(sourceSets["api"].output)

                from("LICENSE")
            }
        }

        // Source artifact, including everything the 'main' does but not compiled.
        defaultArtifactTasks += create("sourcesJar", Jar::class) {
            group = "build"

            archiveClassifier.set("sources")
            from(sourceSets["main"].allSource)
            if (apiExists) {
                from(sourceSets["api"].allSource)
            }

            from("LICENSE")
        }

        val dokkaHtml = tasks.getByName("dokkaHtml")

        // The Javadoc artifact, containing the Dokka output and the LICENSE file.
        defaultArtifactTasks += create("javadocJar", Jar::class) {
            group = "build"

            archiveClassifier.set("javadoc")
            dependsOn(dokkaHtml)
            from(dokkaHtml)

            from("LICENSE")
        }
    }
    this.tasks.block()

    artifacts {
        defaultArtifactTasks.forEach { add("archives", it) }
    }
}
