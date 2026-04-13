package com.taskflow.tasks.service.entities

import java.util.UUID

data class TaskFilters(
    val status: TaskStatus? = null,
    val assigneeId: UUID? = null
)
