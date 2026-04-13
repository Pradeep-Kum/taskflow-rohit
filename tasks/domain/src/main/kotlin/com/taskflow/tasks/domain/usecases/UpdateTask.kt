package com.taskflow.tasks.domain.usecases

import com.taskflow.tasks.domain.entities.Task
import com.taskflow.tasks.domain.entities.UpdateTaskDraft
import com.taskflow.tasks.domain.entities.UpdateTaskRequest
import com.taskflow.tasks.domain.repos.TaskRepo
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*

class UpdateTask(
    private val taskRepo: TaskRepo
) {
    fun update(taskId: UUID, request: UpdateTaskRequest): Task {
        val draft = UpdateTaskDraft(
            title = request.title,
            description = request.description,
            status = request.status,
            priority = request.priority,
            assigneeId = request.assigneeId?.let { UUID.fromString(it) },
            dueDate = request.dueDate?.let {
                LocalDate.parse(it).atTime(LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC)
            }
        )

        return taskRepo.updateTask(taskId, draft)
            ?: throw NoSuchElementException("Task $taskId not found")
    }
}
