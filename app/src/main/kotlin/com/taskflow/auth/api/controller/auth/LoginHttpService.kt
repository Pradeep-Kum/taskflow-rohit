package com.taskflow.auth.api.controller.auth

import com.taskflow.auth.service.entities.LoginRequest
import com.taskflow.auth.service.usecases.LoginService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

class LoginHttpService(
    private val loginService: LoginService
) {
    private val logger = LoggerFactory.getLogger(_root_ide_package_.com.taskflow.auth.api.controller.auth.LoginHttpService::class.java)

    suspend fun handleLogin(call: ApplicationCall) {
        try {
            val request = call.receive<LoginRequest>()

            val response = loginService.login(request)

            call.respond(HttpStatusCode.OK, response)

        } catch (e: Exception) {
            logger.error("Login endpoint failed", e)
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
        }
    }
}