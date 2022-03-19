import api.repdep.*

fun repositories() {
    maven("jitpack.io")
}

fun dependencies() {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
}
