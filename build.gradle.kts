plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("org.graalvm.buildtools.native") version "0.9.25"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.12"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
}

kotlin {
//    jvmToolchain(21)
}


graalvmNative {
    binaries {
        named("main") {
            mainClass.set("org.example.MainKt")
            buildArgs.addAll(listOf(
                "--no-fallback",
                "--static",
                "--libc=musl",
                "--enable-https"
            ))
        }
    }
}
