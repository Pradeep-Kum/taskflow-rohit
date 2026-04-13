plugins {
    kotlin("jvm") version "2.2.21"
    id("io.ktor.plugin") version "3.0.0" apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
    jacoco
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
    apply(plugin = "jacoco")

    // Set Java version for everyone in one place
    kotlin {
        jvmToolchain(17)
    }

    jacoco {
        toolVersion = "0.8.12"
    }

    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.named("jacocoTestReport"))
    }

    tasks.named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.test)

        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
        }
    }

    dependencies {
        testImplementation(kotlin("test"))
    }
}

tasks.register("moduleCoverage") {
    group = "verification"
    description = "Runs tests and generates separate JaCoCo coverage reports for every module."
    dependsOn(subprojects.map { it.tasks.named("jacocoTestReport") })
}
