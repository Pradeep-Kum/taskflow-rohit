package com.taskflow.auth.domain.entities

data class LoginRequest(
    val email: String,
    val password: String
)