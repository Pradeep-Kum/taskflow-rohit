package com.taskflow.auth.service.entities

data class AuthResponse(
    val token: String,
    val user: com.taskflow.auth.service.entities.UserProfile
)