package com.taskflow.tasks.domain.usecases

import com.taskflow.domain.entities.CreateTaskRequest
import com.taskflow.domain.entities.Task
import com.taskflow.domain.entities.CreateTaskDraft
import com.taskflow.domain.entities.TaskPriority
import com.taskflow.domain.entities.TaskStatus
import com.taskflow.domain.repos.TaskRepo
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.UUID

class CreateTask(
    private val taskRepo: TaskRepo
) {

    fun create(projectId: UUID, request: CreateTaskRequest): Task {
        val draft = CreateTaskDraft(
            title = request.title,
            description = request.description,
            status = TaskStatus.TO_DO,
            priority = TaskPriority.valueOf(request.priority.uppercase()),
            projectId = projectId,
            assigneeId = request.assigneeId?.let { UUID.fromString(it) },
            dueDate = request.dueDate?.let {
                LocalDate.parse(it).atTime(LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC)
            }
        )

        return taskRepo.createTask(draft)
    }
}