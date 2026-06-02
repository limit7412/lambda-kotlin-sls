plugins {
    kotlin("multiplatform") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.12"

kotlin {
    // Lambda(provided.al2023, x86_64) 向けに Kotlin/Native で単一実行ファイルを生成する。
    // 生成物 build/bin/linuxX64/releaseExecutable/bootstrap.kexe を bootstrap として配置する。
    linuxX64 {
        binaries {
            executable {
                entryPoint = "org.example.main"
                baseName = "bootstrap"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
    }
}
