plugins {
  java
  kotlin("jvm") version "1.7.20"
  id("org.mikeneck.graalvm-native-image") version "v1.4.0"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.slf4j:slf4j-simple:2.0.3")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
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
    "--libc=musl",
    "--enable-https"
  )
}
