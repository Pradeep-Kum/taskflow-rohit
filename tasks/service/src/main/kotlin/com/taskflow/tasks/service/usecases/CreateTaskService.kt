package com.taskflow.tasks.service.usecases

import com.taskflow.domain.usecases.CreateTask
import com.taskflow.service.entities.CreateTaskRequest
import org.slf4j.LoggerFactory
import com.taskflow.service.entities.Task
import com.taskflow.service.mappers.toDomain
import com.taskflow.service.mappers.toService
import java.util.UUID

class CreateTaskService(
    private val createTask: CreateTask
) {
    private val logger = LoggerFactory.getLogger(CreateTaskService::class.java)

    fun createTask(projectId: UUID, request: CreateTaskRequest): Task {
        return try {
            logger.info("Creating task for project: $projectId")

            val createdTask = createTask.create(projectId, request.toDomain())

            createdTask.toService()

        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for project $projectId", e)
            throw e
        }
    }
}