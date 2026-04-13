package com.taskflow.auth.service.usecases

import com.taskflow.auth.domain.usecases.RegisterUser
import com.taskflow.auth.service.entities.AuthResponse
import com.taskflow.auth.service.entities.RegisterRequest
import com.taskflow.auth.service.mappers.toDomainRegisterRequest
import com.taskflow.auth.service.mappers.toServiceResponse
import org.slf4j.LoggerFactory

class RegisterService(
    private val registerUser: RegisterUser
) {
    private val logger = LoggerFactory.getLogger(RegisterService::class.java)

    fun register(request: RegisterRequest): AuthResponse {
        return try {
            logger.info("Initiating registration for email: ${request.email}")

            val domainResult = registerUser.execute(request.toDomainRegisterRequest())

            domainResult.toServiceResponse()

        } catch (e: Exception) {
            logger.error("Registration service failure for ${request.email}", e)
            throw e
        }
    }
}
