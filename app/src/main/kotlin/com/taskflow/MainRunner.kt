package com.taskflow

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.taskflow.api.controller.project.CreateProjectHttpService
import com.taskflow.api.controller.project.DeleteProjectHttpService
import com.taskflow.api.controller.project.GetProjectHttpService
import com.taskflow.api.controller.project.GetProjectsHttpService
import com.taskflow.api.controller.project.UpdateProjectHttpService
import com.taskflow.auth.api.controller.auth.LoginHttpService
import com.taskflow.auth.api.controller.auth.RegisterHttpService
import com.taskflow.auth.api.controller.task.CreateTaskHttpService
import com.taskflow.auth.api.controller.task.DeleteTaskHttpService
import com.taskflow.auth.api.controller.task.GetTaskHttpService
import com.taskflow.auth.api.controller.task.UpdateTaskHttpService
import com.taskflow.auth.api.routes.authRoutes
import com.taskflow.auth.api.routes.projectRouting
import com.taskflow.auth.api.routes.taskRouting
import com.taskflow.auth.di.appModule
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import org.koin.core.context.GlobalContext
import org.koin.ktor.plugin.Koin

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    val server = embeddedServer(Netty, port = port, module = Application::module)
    Runtime.getRuntime().addShutdownHook(Thread {
        server.stop(gracePeriodMillis = 5_000, timeoutMillis = 10_000)
    })
    server.start(wait = true)
}

fun Application.module() {
    val jwtSecret = requiredEnv("JWT_SECRET")
    val jwtIssuer = env("JWT_ISSUER", "taskflow")

    install(ContentNegotiation) {
        jackson {
            registerModule(KotlinModule.Builder().build())
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
        }
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("user_id").asString()
                val email = credential.payload.getClaim("email").asString()
                if (!userId.isNullOrBlank() && !email.isNullOrBlank()) {
                    io.ktor.server.auth.jwt.JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "unauthorized"))
            }
        }
    }

    install(Koin) {
        modules(appModule)
    }

    val koin = GlobalContext.get()

    routing {
        authRoutes(
            registerHttpService = koin.get<RegisterHttpService>(),
            loginHttpService = koin.get<LoginHttpService>()
        )
        authenticate("auth-jwt") {
            projectRouting(
                getProjectsHttpService = koin.get<GetProjectsHttpService>(),
                getProjectHttpService = koin.get<GetProjectHttpService>(),
                createProjectHttpService = koin.get<CreateProjectHttpService>(),
                updateProjectHttpService = koin.get<UpdateProjectHttpService>(),
                deleteProjectHttpService = koin.get<DeleteProjectHttpService>()
            )
            taskRouting(
                getTaskHttpService = koin.get<GetTaskHttpService>(),
                createTaskHttpService = koin.get<CreateTaskHttpService>(),
                updateTaskHttpService = koin.get<UpdateTaskHttpService>(),
                deleteTaskHttpService = koin.get<DeleteTaskHttpService>()
            )
        }
    }
}

private fun env(name: String, default: String): String {
    return System.getenv(name)?.takeIf { it.isNotBlank() } ?: default
}

private fun requiredEnv(name: String): String {
    return System.getenv(name)?.takeIf { it.isNotBlank() }
        ?: error("$name environment variable is required")
}
