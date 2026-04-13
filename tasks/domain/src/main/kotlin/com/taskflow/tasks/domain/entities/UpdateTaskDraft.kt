package com.taskflow.tasks.domain.entities

import java.time.OffsetDateTime
import java.util.UUID

data class UpdateTaskDraft (
    val title: String? = null,
    val description: String? = null,
    val status: TaskStatus? = null,
    val priority: TaskPriority? = null,
    val assigneeId: UUID? = null,
    val dueDate: OffsetDateTime? = null
)