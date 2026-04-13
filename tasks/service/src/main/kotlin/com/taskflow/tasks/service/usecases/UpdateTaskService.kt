package com.taskflow.tasks.service.usecases

import com.taskflow.domain.usecases.UpdateTask
import com.taskflow.service.entities.Task
import com.taskflow.service.entities.UpdateTaskRequest
import com.taskflow.service.mappers.toDomain
import com.taskflow.service.mappers.toService
import org.slf4j.LoggerFactory
import java.util.*

class UpdateTaskService(
    private val updateTask: UpdateTask
) {
    private val logger = LoggerFactory.getLogger(CreateTaskService::class.java)

    fun updateTask(taskId: UUID, request: UpdateTaskRequest): Task {
        return try {
            logger.info("Creating task for task: $taskId")

            val createdTask = updateTask.update(taskId, request.toDomain())

            createdTask.toService()

        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for task $taskId", e)
            throw e
        }
    }
}