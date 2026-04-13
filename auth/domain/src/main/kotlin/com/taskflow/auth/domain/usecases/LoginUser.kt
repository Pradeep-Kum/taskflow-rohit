package com.taskflow.auth.domain.usecases

import com.taskflow.domain.entities.AuthResponse
import com.taskflow.domain.entities.LoginRequest
import com.taskflow.domain.entities.UserProfile
import com.taskflow.domain.interfaces.PasswordHasher
import com.taskflow.domain.interfaces.TokenService
import com.taskflow.domain.repos.AuthRepo

class LoginUser(
    private val userRepo: AuthRepo,
    private val passwordHasher: PasswordHasher,
    private val tokenService: TokenService
) {
    fun execute(request: LoginRequest): AuthResponse {
        val user = userRepo.findByEmail(request.email)
            ?: throw NoSuchElementException("Invalid email or password")

        if (!passwordHasher.check(request.password, user.passwordHash)) {
            throw NoSuchElementException("Invalid email or password")
        }

        val token = tokenService.generate(user.id)

        return AuthResponse(token, UserProfile(user.id, user.name, user.email))
    }
}