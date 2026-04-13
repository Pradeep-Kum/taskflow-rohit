package com.taskflow.tasks.service.usecases

import com.taskflow.tasks.domain.usecases.UpdateTask
import com.taskflow.tasks.service.entities.Task
import com.taskflow.tasks.service.entities.UpdateTaskRequest
import com.taskflow.tasks.service.mappers.toDomain
import com.taskflow.tasks.service.mappers.toService
import org.slf4j.LoggerFactory
import java.util.*

class UpdateTaskService(
    private val updateTask: UpdateTask
) {
    private val logger = LoggerFactory.getLogger(UpdateTaskService::class.java)

    fun updateTask(taskId: UUID, request: UpdateTaskRequest): Task {
        return try {
            logger.info("Updating task: $taskId")
            updateTask.update(taskId, request.toDomain()).toService()

        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for task $taskId", e)
            throw e
        }
    }
}
