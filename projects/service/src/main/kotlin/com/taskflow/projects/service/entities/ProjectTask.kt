package com.taskflow.projects.service.entities

import java.time.OffsetDateTime
import java.util.UUID

data class ProjectTask(
    val id: UUID,
    val title: String,
    val description: String?,
    val status: String,
    val priority: String,
    val assigneeId: UUID?,
    val dueDate: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
