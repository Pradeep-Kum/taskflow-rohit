package com.taskflow.auth.domain.usecases

import com.taskflow.auth.domain.entities.AuthResponse
import com.taskflow.auth.domain.entities.RegisterRequest
import com.taskflow.auth.domain.entities.UserProfile
import com.taskflow.auth.domain.interfaces.PasswordHasher
import com.taskflow.auth.domain.interfaces.TokenService
import com.taskflow.auth.domain.repos.AuthRepo
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

        val token = tokenService.generate(newUser.id, newUser.email)

        return AuthResponse(token, UserProfile(newUser.id, newUser.name, newUser.email))
    }
}
