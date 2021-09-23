plugins {
  java
  kotlin("jvm") version "1.5.10"
  id("org.mikeneck.graalvm-native-image") version "v1.4.0"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.slf4j:slf4j-simple:1.7.28")
}

nativeImage {
  graalVmHome = System.getenv("JAVA_HOME")
  buildType { build ->
    build.executable(main = "MainKt")
  }
  executableName = "bootstrap"
  outputDirectory = file("$buildDir/executable")
}
