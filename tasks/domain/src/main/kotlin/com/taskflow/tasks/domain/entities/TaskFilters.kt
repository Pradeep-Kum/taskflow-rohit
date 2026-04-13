package com.taskflow.tasks.domain.entities

import java.util.UUID

data class TaskFilters(
    val status: TaskStatus? = null,
    val assigneeId: UUID? = null
)
