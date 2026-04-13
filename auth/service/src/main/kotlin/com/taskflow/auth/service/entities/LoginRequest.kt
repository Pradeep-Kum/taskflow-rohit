package com.taskflow.auth.service.entities

data class LoginRequest(
    val email: String,
    val password: String
)