package com.taskflow.auth.domain.entities

data class AuthResponse(
    val token: String,
    val user: com.taskflow.auth.domain.entities.UserProfile
)