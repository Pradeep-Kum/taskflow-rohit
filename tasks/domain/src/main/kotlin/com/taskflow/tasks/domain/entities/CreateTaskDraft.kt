package com.taskflow.tasks.domain.entities

import java.time.OffsetDateTime
import java.util.UUID

data class CreateTaskDraft(
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: TaskPriority,
    val projectId: UUID,
    val assigneeId: UUID?,
    val createdById: UUID,
    val dueDate: OffsetDateTime?
)
