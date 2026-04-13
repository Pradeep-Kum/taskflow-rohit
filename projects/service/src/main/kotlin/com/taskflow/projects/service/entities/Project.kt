package com.taskflow.projects.service.entities

import java.time.OffsetDateTime
import java.util.UUID

data class Project(
    val id: UUID,
    val name: String,
    val description: String?,
    val ownerId: UUID,
    val createdAt: OffsetDateTime
)
