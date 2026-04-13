package com.taskflow.auth.api.routes

import com.taskflow.api.controller.auth.LoginHttpService
import com.taskflow.api.controller.auth.RegisterHttpService
import io.ktor.server.routing.*

fun Route.authRoutes(
    registerHttpService: RegisterHttpService,
    loginHttpService: LoginHttpService
) {
    route("/auth") {
        post("/register") {
            registerHttpService.handleRegister(call)
        }
        post("/login") {
            loginHttpService.handleLogin(call)
        }
    }
}