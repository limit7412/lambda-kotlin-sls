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
    // Lambda(provided.al2023) 向けに Kotlin/Native で単一実行ファイルを生成する。
    // arm64(Graviton) では linuxArm64、x86_64 では linuxX64 を使用する。
    // 生成物 build/bin/<target>/releaseExecutable/bootstrap.kexe を bootstrap として配置する。
    // linuxArm64 は linuxX64 ホストからクロスコンパイルできるため QEMU は不要。
    listOf(linuxX64(), linuxArm64()).forEach { target ->
        target.binaries {
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
