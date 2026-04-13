package com.taskflow.tasks.service.entities

data class CreateTaskRequest(
    val title: String,
    val description: String?,
    val priority: String,
    val assigneeId: String?,
    val dueDate: String?
)