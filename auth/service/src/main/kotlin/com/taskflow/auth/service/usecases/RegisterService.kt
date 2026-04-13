package com.taskflow.auth.service.usecases

import com.taskflow.domain.usecases.RegisterUser
import com.taskflow.service.entities.AuthResponse
import com.taskflow.service.entities.RegisterRequest
import com.taskflow.service.mappers.toDomainRegisterRequest
import com.taskflow.service.mappers.toServiceResponse
import org.slf4j.LoggerFactory

class RegisterService(
    private val registerUser: RegisterUser
) {
    private val logger = LoggerFactory.getLogger(_root_ide_package_.com.taskflow.auth.service.usecases.RegisterService::class.java)

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