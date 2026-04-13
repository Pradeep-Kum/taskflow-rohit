package com.taskflow.tasks.domain.entities

data class CreateTaskRequest(
    val title: String,
    val description: String?,
    val priority: String,
    val assigneeId: String?,
    val dueDate: String?
)