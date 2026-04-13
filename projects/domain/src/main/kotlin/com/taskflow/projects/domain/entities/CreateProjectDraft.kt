package com.taskflow.projects.domain.entities

import java.util.UUID

data class CreateProjectDraft(
    val name: String,
    val description: String?,
    val ownerId: UUID
)
