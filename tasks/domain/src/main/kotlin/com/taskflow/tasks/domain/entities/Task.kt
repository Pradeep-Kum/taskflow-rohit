package com.taskflow.tasks.domain.entities

import java.time.OffsetDateTime
import java.util.*

data class Task(
    val id: UUID,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: TaskPriority,
    val projectId: UUID,
    val assigneeId: UUID?,
    val dueDate: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)