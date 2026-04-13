plugins {
    kotlin("jvm") version "2.2.21"
    id("io.ktor.plugin") version "3.0.0" apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
}

val koin_version = "4.0.0"

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // Set Java version for everyone in one place
    kotlin {
        jvmToolchain(17)
    }

    tasks.test {
        useJUnitPlatform()
    }

    dependencies {
        testImplementation(kotlin("test"))
    }
}