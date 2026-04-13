package com.taskflow.tasks.domain.usecases

import com.taskflow.tasks.domain.entities.CreateTaskDraft
import com.taskflow.tasks.domain.entities.CreateTaskRequest
import com.taskflow.tasks.domain.entities.Task
import com.taskflow.tasks.domain.entities.TaskPriority
import com.taskflow.tasks.domain.entities.TaskStatus
import com.taskflow.tasks.domain.repos.TaskRepo
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.UUID

class CreateTask(
    private val taskRepo: TaskRepo
) {

    fun create(projectId: UUID, createdById: UUID, request: CreateTaskRequest): Task {
        val draft = CreateTaskDraft(
            title = request.title,
            description = request.description,
            status = TaskStatus.TODO,
            priority = TaskPriority.valueOf(request.priority.uppercase()),
            projectId = projectId,
            assigneeId = request.assigneeId?.let { UUID.fromString(it) },
            createdById = createdById,
            dueDate = request.dueDate?.let {
                LocalDate.parse(it).atTime(LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC)
            }
        )

        return taskRepo.createTask(draft)
    }
}
