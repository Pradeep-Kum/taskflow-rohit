package com.taskflow

import com.taskflow.infrastructure.DatabaseFactory
import com.taskflow.di.appModule
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // 1. Initialize DB
    DatabaseFactory.init()

    // 2. Install Koin
    install(Koin) {
        modules(appModule)
    }

    // 3. Install JSON Serialization
    install(ContentNegotiation) {
        json()
    }

    // 4. Load your module routes (We will build these in Milestone 2)
    // configureAuthRoutes()
}