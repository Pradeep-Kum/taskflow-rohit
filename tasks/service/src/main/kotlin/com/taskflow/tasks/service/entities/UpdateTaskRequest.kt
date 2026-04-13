package com.taskflow.tasks.service.entities

data class UpdateTaskRequest(
    val title: String? = null,
    val description: String? = null,
    val status: TaskStatus? = null,
    val priority: TaskPriority? = null,
    val assigneeId: String? = null,
    val dueDate: String? = null
)