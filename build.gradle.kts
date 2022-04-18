@file:Suppress("UNUSED_VARIABLE")

import plugins.ShadowJar
import java.net.URL
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

plugins {
    with(Plugins) {
        // Language Plugins
        `java-library`
        kotlin("jvm") version KOTLIN

        // Git Repo Information
        id("org.ajoberstar.grgit") version GRGIT

        // Token Replacement
        id("net.kyori.blossom") version BLOSSOM

        // Dependency Shading
        id("com.github.johnrengelman.shadow") version SHADOW

        // Code Quality
        id("org.jlleitschuh.gradle.ktlint") version KTLINT

        // Documentation Generation
        id("org.jetbrains.dokka") version DOKKA

        // Maven Publication
        id("io.github.gradle-nexus.publish-plugin") version NEXUS_PUBLISH
        `maven-publish`
        signing
    }
}

// What JVM version should this project compile to
val targetVersion = "1.8"
// What JVM version this project is written in
val sourceVersion = "1.8"
// Which source-sets to add.
val additionalSourceSets: Array<String> = arrayOf(
    "api"
)

// Handle configurations lower down
configurations()

// Project Dependencies
dependencies {
    val include by configurations

    with(Dependencies) {
        kotlinModules.forEach {
            implementation("org.jetbrains.kotlin", "kotlin-$it", KOTLIN)
        }
        testImplementation("org.jetbrains.kotlin", "kotlin-test", KOTLIN)
    }
}

// Maven Repositories
repositories {
    mavenLocal()
    mavenCentral()

    Repositories.mavenUrls.forEach(::maven)
}

fun Project.configurations() {
    // Add `include` configuration for ShadowJar
    configurations {
        val include by creating
        // don't include in maven pom
        compileOnly.get().extendsFrom(include)
        // but also work in tests
        testImplementation.get().extendsFrom(include)

        // Makes all the configurations use the same Kotlin version.
        all {
            resolutionStrategy.eachDependency {
                if (requested.group == "org.jetbrains.kotlin") {
                    useVersion(Dependencies.KOTLIN)
                }
            }
        }
    }
}

group = Coordinates.GROUP
version = Coordinates.VERSION

// Generate the additional source sets
additionalSourceSets.forEach(::createSourceSet)

fun createSourceSet(name: String, sourceRoot: String = "src/$name") {
    sourceSets {
        val main by sourceSets
        val test by sourceSets

        val sourceSet = create(name) {
            java.srcDir("$sourceRoot/kotlin")
            resources.srcDir("$sourceRoot/resources")

            this.compileClasspath += main.compileClasspath
            this.runtimeClasspath += main.runtimeClasspath
        }

        arrayOf(main, test).forEach {
            it.compileClasspath += sourceSet.output
            it.runtimeClasspath += sourceSet.output
        }
    }
}

// The latest commit ID
val buildRevision: String = grgit.log()[0].id ?: "dev"

// Disable unneeded rules
ktlint {
    this.disabledRules.addAll(
        "no-wildcard-imports",
        "filename"
    )
}

blossom {
    mapOf(
        "project.name" to Coordinates.NAME,
        "project.version" to Coordinates.VERSION,
        "project.desc" to Coordinates.DESC,
        "project.rev" to buildRevision,
    ).mapKeys { "@${it.key}@" }.forEach { (key, value) ->
        replaceToken(key, value)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    // Configure JVM versions
    compileKotlin {
        kotlinOptions {
            jvmTarget = targetVersion
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn",
            )
        }
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
            displayName.set("${Coordinates.NAME} on ${Coordinates.GIT_HOST}")
            includes.from(moduleFile.path)

            skipDeprecated.set(false)
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(true)
            suppressObviousFunctions.set(true)

            // Link the source to the documentation
            sourceLink {
                localDirectory.set(file("src"))
                remoteUrl.set(URL("https://${Coordinates.GIT_HOST}/${Coordinates.REPO_ID}/tree/trunk/src"))
            }

            // External documentation link template
//            externalDocumentationLink {
//                url.set(URL("https://javadoc.io/doc/net.java.dev.jna/jna/5.10.0/"))
//            }
        }
    }

    // The original artifact, we just have to add the API source output and the
    // LICENSE file.
    jar {
        fun normalizeVersion(versionLiteral: String): String {
            val regex = Regex("(\\d+\\.\\d+\\.\\d+).*")
            val match = regex.matchEntire(versionLiteral)
            require(match != null) {
                "Version '$versionLiteral' does not match version pattern, e.g. 1.0.0-QUALIFIER"
            }
            return match.groupValues[1]
        }

        val buildTimeAndDate = OffsetDateTime.now()
        val buildDate = DateTimeFormatter.ISO_LOCAL_DATE.format(buildTimeAndDate)
        val buildTime = DateTimeFormatter.ofPattern("HH:mm:ss.SSSZ").format(buildTimeAndDate)

        val javaVersion = System.getProperty("java.version")
        val javaVendor = System.getProperty("java.vendor")
        val javaVmVersion = System.getProperty("java.vm.version")

        with(Coordinates) {
            mapOf(
                "Created-By" to "$javaVersion ($javaVendor $javaVmVersion)",
                "Build-Date" to buildDate,
                "Build-Time" to buildTime,
                "Build-Revision" to buildRevision,

                "Specification-Title" to project.name,
                "Specification-Version" to normalizeVersion(project.version.toString()),
                "Specification-Vendor" to VENDOR,

                "Implementation-Title" to NAME,
                "Implementation-Version" to VERSION,
                "Implementation-Vendor" to VENDOR,

                "Bundle-Name" to NAME,
                "Bundle-Description" to DESC,
                "Bundle-DocURL" to "https://$GIT_HOST/$REPO_ID",
                "Bundle-Vendor" to VENDOR,
                "Bundle-SymbolicName" to "$GROUP.$NAME"
            ).forEach { (k, v) ->
                manifest.attributes[k] = v
            }
        }

        additionalSourceSets.forEach {
            from(sourceSets[it].output)
        }
        from("LICENSE")
    }

    additionalSourceSets.forEach {
        // Custom artifact, only including the output of
        // the source set and the LICENSE file.
        create(it + "Jar", Jar::class) {
            group = "build"

            archiveClassifier.set(it)
            from(sourceSets[it].output)

            from("LICENSE")
        }
    }

    // Source artifact, including everything the 'main' does but not compiled.
    create("sourcesJar", Jar::class) {
        group = "build"

        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)

        additionalSourceSets.forEach {
            from(sourceSets[it].allSource)
        }

        this.manifest.from(jar.get().manifest)

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

    // Configure ShadowJar
    shadowJar {
        val include by project.configurations

        this.configurations.clear()
        this.configurations += include

        // Add the API source set to the ShadowJar
        additionalSourceSets.forEach {
            from(sourceSets[it].output)
        }
        from("LICENSE")

        this.archiveClassifier.set(ShadowJar.classifier)
        this.manifest.inheritFrom(jar.get().manifest)

        ShadowJar.packageRemappings.forEach(this::relocate)
    }

    afterEvaluate {
        // Task priority
        val publishToSonatype by getting
        val closeAndReleaseSonatypeStagingRepository by getting

        closeAndReleaseSonatypeStagingRepository
            .mustRunAfter(publishToSonatype)

        // Wrapper task since calling both one after the other in IntelliJ
        // seems to cause some problems.
        create("releaseToSonatype") {
            group = "publishing"

            dependsOn(
                publishToSonatype,
                closeAndReleaseSonatypeStagingRepository
            )
        }
    }
}

// Define the default artifacts' tasks
val defaultArtifactTasks = arrayOf(
    tasks["sourcesJar"],
    tasks["javadocJar"]
).also { arr ->
    additionalSourceSets.forEach { set ->
        arr.plus(tasks[set + "Jar"])
    }
}

// Declare the artifacts
artifacts {
    defaultArtifactTasks.forEach(::archives)
    archives(tasks.shadowJar)
}

publishing.publications {
    // Sets up the Maven integration.
    create("mavenJava", MavenPublication::class.java) {
        from(components["java"])
        defaultArtifactTasks.forEach(::artifact)

        with(Coordinates) {
            pom {
                name.set(NAME)
                description.set(DESC)
                url.set("https://$GIT_HOST/$REPO_ID")

                with(Pom) {
                    licenses {
                        licenses.forEach {
                            license {
                                name.set(it.name)
                                url.set(it.url)
                                distribution.set(it.distribution)
                            }
                        }
                    }

                    developers {
                        developers.forEach {
                            developer {
                                id.set(it.id)
                                name.set(it.name)
                            }
                        }
                    }
                }

                scm {
                    connection.set("scm:git:git://$GIT_HOST/$REPO_ID.git")
                    developerConnection.set("scm:git:ssh://$GIT_HOST/$REPO_ID.git")
                    url.set("https://$GIT_HOST/$REPO_ID")
                }
            }
        }

        // Configure the signing extension to sign this Maven artifact.
        signing {
            isRequired = project.properties["signing.keyId"] != null
            sign(this@create)
        }
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
