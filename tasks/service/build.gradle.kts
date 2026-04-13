plugins {
    kotlin("jvm") version "2.2.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":tasks-domain"))

    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2")
    implementation("org.slf4j:slf4j-api:2.0.16")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
