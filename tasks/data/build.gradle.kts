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

    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")

    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.jetbrains.exposed:exposed-java-time:0.50.1")
    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.flywaydb:flyway-database-postgresql:10.10.0")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
