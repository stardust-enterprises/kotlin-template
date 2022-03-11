import java.net.URL

plugins {
    // Language Plugins
    `java-library`
    kotlin("jvm") version Plugins.KOTLIN

    // Code Quality
    id("org.jlleitschuh.gradle.ktlint") version Plugins.KTLINT

    // Documentation Generation
    id("org.jetbrains.dokka") version Plugins.DOKKA

    // Maven Publication
    id("io.github.gradle-nexus.publish-plugin") version Plugins.NEXUS_PUBLISH
    `maven-publish`
    signing
}

group = Coordinates.GROUP
version = Coordinates.VERSION

// What JVM version should this project compile to
val targetVersion = "1.8"
// What JVM version this project is written in
val sourceVersion = "1.8"
// Should we generate an /api/ source set
val apiSourceSet = true

// Maven Repositories
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

// Project Dependencies
dependencies {
    Dependencies.kotlinModules.forEach {
        implementation("org.jetbrains.kotlin", "kotlin-$it", Plugins.KOTLIN)
    }
    testImplementation("org.jetbrains.kotlin", "kotlin-test", Plugins.KOTLIN)
}

// Generate the /api/ source set
if (apiSourceSet) {
    sourceSets {
        val name = "api"

        val main by sourceSets
        val test by sourceSets

        val sourceSet = create(name) {
            java.srcDir("src/$name/kotlin")
            resources.srcDir("src/$name/resources")

            this.compileClasspath += main.compileClasspath
            this.runtimeClasspath += main.runtimeClasspath
        }

        arrayOf(main, test).forEach {
            it.compileClasspath += sourceSet.output
            it.runtimeClasspath += sourceSet.output
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    // Configure JVM versions
    compileKotlin {
        kotlinOptions.jvmTarget = targetVersion
    }
    compileJava {
        targetCompatibility = targetVersion
        sourceCompatibility = sourceVersion
    }

    dokkaHtml {
        val moduleFile = File(projectDir, "MODULE.temp.md")

        run {
            // In order to have a description on the rendered docs, we have to have
            // a file with the # Module thingy in it. That's what we're
            // automagically creating here.

            doFirst {
                moduleFile.writeText("# Module ${Coordinates.NAME}\n${Coordinates.DESC}")
            }

            doLast {
                moduleFile.delete()
            }
        }

        moduleName.set(Coordinates.NAME)

        dokkaSourceSets.configureEach {
            displayName.set("${Coordinates.NAME} github")
            includes.from(moduleFile.path)

            skipDeprecated.set(false)
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(true)
            suppressObviousFunctions.set(true)

            // Link the source to the documentation
            sourceLink {
                localDirectory.set(file("src"))
                remoteUrl.set(URL("https://github.com/${Coordinates.REPO_ID}/tree/trunk/src"))
            }

            // JNA external documentation links
            externalDocumentationLink {
                url.set(URL("https://javadoc.io/doc/net.java.dev.jna/jna/5.10.0/"))
            }
        }
    }

    // The original artifact, we just have to add the API source output and the
    // LICENSE file.
    jar {
        if (apiSourceSet) {
            from(sourceSets["api"].output)
        }
        from("LICENSE")
    }

    if (apiSourceSet) {
        // API artifact, only including the output of the API source and the
        // LICENSE file.
        create("apiJar", Jar::class) {
            group = "build"

            archiveClassifier.set("api")
            from(sourceSets["api"].output)

            from("LICENSE")
        }
    }

    // Source artifact, including everything the 'main' does but not compiled.
    create("sourcesJar", Jar::class) {
        group = "build"

        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
        if (apiSourceSet) {
            from(sourceSets["api"].allSource)
        }

        from("LICENSE")
    }

    // The Javadoc artifact, containing the Dokka output and the LICENSE file.
    create("javadocJar", Jar::class) {
        group = "build"

        val dokkaHtml = getByName("dokkaHtml")

        archiveClassifier.set("javadoc")
        dependsOn(dokkaHtml)
        from(dokkaHtml)

        from("LICENSE")
    }
}

// Define the default artifacts' tasks
val defaultArtifactTasks = arrayOf(
    tasks["sourcesJar"],
    tasks["javadocJar"]
).also {
    if (apiSourceSet) {
        it.plus(tasks["apiJar"])
    }
}

// Declare the artifacts
artifacts {
    defaultArtifactTasks.forEach(::archives)
}

publishing.publications {
    // Sets up the Maven integration.
    create("mavenJava", MavenPublication::class.java) {
        from(components["java"])
        defaultArtifactTasks.forEach(::artifact)

        pom {
            name.set(Coordinates.NAME)
            description.set(Coordinates.DESC)
            url.set("https://github.com/${Coordinates.REPO_ID}")

            licenses {
                Pom.licenses.forEach {
                    license {
                        name.set(it.name)
                        url.set(it.url)
                        distribution.set(it.distribution)
                    }
                }
            }

            developers {
                Pom.developers.forEach {
                    developer {
                        id.set(it.id)
                        name.set(it.name)
                    }
                }
            }

            scm {
                connection.set("scm:git:git://github.com/${Coordinates.REPO_ID}.git")
                developerConnection.set("scm:git:ssh://github.com/${Coordinates.REPO_ID}.git")
                url.set("https://github.com/${Coordinates.REPO_ID}")
            }
        }

        // Configure the signing extension to sign this Maven artifact.
        signing.sign(this)
    }
}

// Configure publishing to Maven Central
nexusPublishing.repositories.sonatype {
    nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
    snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

    // Skip this step if environment variables NEXUS_USERNAME or NEXUS_PASSWORD aren't set.
    username.set(properties["NEXUS_USERNAME"] as? String ?: return@sonatype)
    password.set(properties["NEXUS_PASSWORD"] as? String ?: return@sonatype)
}
