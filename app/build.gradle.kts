plugins {
    application
    kotlin("jvm") version "2.2.21"
    id("io.ktor.plugin") version "3.0.0"
}

application {
    mainClass.set("com.taskflow.MainRunnerKt")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":auth-data"))
    implementation(project(":auth-domain"))
    implementation(project(":auth-service"))
    implementation(project(":projects-data"))
    implementation(project(":projects-domain"))
    implementation(project(":projects-service"))
    implementation(project(":tasks-data"))
    implementation(project(":tasks-domain"))
    implementation(project(":tasks-service"))

    implementation("io.insert-koin:koin-ktor:4.0.0")
    implementation("io.insert-koin:koin-logger-slf4j:4.0.0")

    implementation("io.ktor:ktor-server-netty:3.0.0")
    implementation("io.ktor:ktor-server-auth-jwt:3.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.0.0")
    implementation("io.ktor:ktor-serialization-jackson:3.0.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.jetbrains.exposed:exposed-java-time:0.50.1")
    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.flywaydb:flyway-core:10.10.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.10.0")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
