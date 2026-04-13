package com.taskflow.auth.domain.entities

import java.util.UUID

data class UserProfile(
    val id: UUID,
    val name: String,
    val email: String
)