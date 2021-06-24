plugins {
  java
  kotlin("jvm") version "1.5.10"
  id("org.mikeneck.graalvm-native-image") version "v1.4.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  implementation("org.slf4j:slf4j-simple:1.7.28")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

nativeImage {
//  graalVmHome = System.getenv("JAVA_HOME")
//  mainClass = "com.example.App" // Deprecated, use `buildType.executable.main` as follows instead.
//  buildType { build ->
//    build.executable(main = 'com.example.App')
//  }
  executableName = "my-native-application"
  outputDirectory = file("$buildDir/executable")
  arguments(
    "--no-fallback",
    "--enable-all-security-services",
//    options.traceClassInitialization('com.example.MyDataProvider,com.example.MyDataConsumer'),
    "--initialize-at-run-time=com.example.runtime",
    "--report-unsupported-elements-at-runtime"
  )
}

//generateNativeImageConfig {
//  enabled = true
//  byRunningApplication {
//    stdIn(
//      """
//      |total: 2
//      |contents:
//      |  - name: foo
//      |    size: 2052
//      |""".trimMargin()
//    )
//  }
//  byRunningApplicationWithoutArguments()
//  byRunningApplication {
//    arguments('-h')
//  }
//}
