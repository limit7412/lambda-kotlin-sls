plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
//    id("io.ktor.plugin") version "2.3.11"
    id("org.graalvm.buildtools.native") version "0.9.25"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}

kotlin {
//    jvmToolchain(21)
}

//application {
//    mainClass.set("org.example.MainKt")
//}

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