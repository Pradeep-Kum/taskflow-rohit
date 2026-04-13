package com.taskflow.tasks.service.usecases

import com.taskflow.tasks.domain.usecases.CreateTask
import com.taskflow.tasks.service.entities.CreateTaskRequest
import com.taskflow.tasks.service.entities.Task
import com.taskflow.tasks.service.mappers.toDomain
import com.taskflow.tasks.service.mappers.toService
import org.slf4j.LoggerFactory
import java.util.UUID

class CreateTaskService(
    private val createTask: CreateTask
) {
    private val logger = LoggerFactory.getLogger(CreateTaskService::class.java)

    fun createTask(projectId: UUID, createdById: UUID, request: CreateTaskRequest): Task {
        return try {
            logger.info("Creating task for project: $projectId")

            val createdTask = createTask.create(projectId, createdById, request.toDomain())

            createdTask.toService()

        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for project $projectId", e)
            throw e
        }
    }
}
