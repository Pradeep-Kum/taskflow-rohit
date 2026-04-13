package com.taskflow.auth.service.usecases

import com.taskflow.service.entities.LoginRequest
import com.taskflow.domain.usecases.LoginUser
import com.taskflow.service.entities.AuthResponse
import com.taskflow.service.mappers.toDomainLoginRequest
import com.taskflow.service.mappers.toServiceResponse
import org.slf4j.LoggerFactory


class LoginService(
    private val loginUser: LoginUser
) {
    private val logger = LoggerFactory.getLogger(_root_ide_package_.com.taskflow.auth.service.usecases.LoginService::class.java)

    fun login(request: LoginRequest): AuthResponse {
        return try {
            logger.info("Initiating login for email: ${request.email}")

            val domainResult = loginUser.execute(request.toDomainLoginRequest())

            domainResult.toServiceResponse()

        } catch (e: Exception) {
            logger.error("Login service failure for ${request.email}", e)
            throw e
        }
    }
}