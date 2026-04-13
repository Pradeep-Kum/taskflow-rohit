package com.taskflow.auth.api.routes

import com.taskflow.auth.api.controller.auth.LoginHttpService
import com.taskflow.auth.api.controller.auth.RegisterHttpService
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

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
