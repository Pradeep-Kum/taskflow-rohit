package com.taskflow.auth.domain.usecases

import com.taskflow.domain.entities.AuthResponse
import com.taskflow.domain.entities.RegisterRequest
import com.taskflow.domain.entities.UserProfile
import com.taskflow.domain.interfaces.PasswordHasher
import com.taskflow.domain.interfaces.TokenService
import com.taskflow.domain.repos.AuthRepo
import java.util.UUID

class RegisterUser(
    private val userRepo: AuthRepo,
    private val passwordHasher: PasswordHasher,
    private val tokenService: TokenService
) {
    fun execute(request: RegisterRequest): AuthResponse {
        val hash = passwordHasher.hash(request.password)
        val newUser = userRepo.createUser(UUID.randomUUID(), request.name, request.email, hash)
            ?: throw IllegalStateException("Failed to create user")

        val token = tokenService.generate(newUser.id)

        return AuthResponse(token, UserProfile(newUser.id, newUser.name, newUser.email))
    }
}