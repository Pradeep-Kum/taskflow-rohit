package com.taskflow.auth.api.controller.auth

import com.taskflow.auth.service.entities.RegisterRequest
import com.taskflow.auth.service.usecases.RegisterService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

class RegisterHttpService(
    private val registerService: RegisterService
) {
    private val logger = LoggerFactory.getLogger(_root_ide_package_.com.taskflow.auth.api.controller.auth.RegisterHttpService::class.java)

    suspend fun handleRegister(call: ApplicationCall) {
        try {
            val request = call.receive<RegisterRequest>()

            val response = registerService.register(request)

            call.respond(HttpStatusCode.Created, response)

        } catch (e: Exception) {
            logger.error("Registration endpoint failed", e)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Registration failed")))
        }
    }
}