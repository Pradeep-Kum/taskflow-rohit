package com.taskflow.auth.domain.usecases

import com.taskflow.auth.domain.entities.AuthResponse
import com.taskflow.auth.domain.entities.LoginRequest
import com.taskflow.auth.domain.entities.UserProfile
import com.taskflow.auth.domain.interfaces.PasswordHasher
import com.taskflow.auth.domain.interfaces.TokenService
import com.taskflow.auth.domain.repos.AuthRepo

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

        val token = tokenService.generate(user.id, user.email)

        return AuthResponse(token, UserProfile(user.id, user.name, user.email))
    }
}
