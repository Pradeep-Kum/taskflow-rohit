package com.taskflow.auth.service.entities

import java.util.UUID

data class UserProfile(
    val id: UUID,
    val name: String,
    val email: String
)