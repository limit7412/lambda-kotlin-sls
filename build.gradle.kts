plugins {
  java
  kotlin("jvm") version "1.5.20"
  id("org.mikeneck.graalvm-native-image") version "v1.4.0"
  kotlin("plugin.serialization") version "1.5.20"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.slf4j:slf4j-simple:1.7.28")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
}

nativeImage {
  graalVmHome = System.getenv("JAVA_HOME")
  buildType { build ->
    build.executable(main = "MainKt")
  }
  executableName = "bootstrap"
  outputDirectory = file("$buildDir/executable")
  arguments(
    "--no-fallback",
    "--static",
    "--enable-https"
  )
}
