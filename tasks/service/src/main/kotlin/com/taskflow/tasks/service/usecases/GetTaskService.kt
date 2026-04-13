package com.taskflow.tasks.service.usecases

import com.taskflow.domain.entities.TaskStatus
import org.slf4j.LoggerFactory
import com.taskflow.service.entities.Task
import com.taskflow.domain.usecases.GetTask
import com.taskflow.service.mappers.toService
import java.util.UUID

class GetTaskService(
    private val getTask: GetTask
) {
    private val logger = LoggerFactory.getLogger(GetTaskService::class.java)

    fun getProjectTasks(projectId: UUID, status: String?, assigneeId: UUID?): List<Task> {
        return try {
            logger.info("Fetching tasks for project: $projectId")

            val status = status?.let {
                TaskStatus.valueOf(it.uppercase())
            } ?: throw IllegalArgumentException("Status is required")

            val domainTasks = getTask.getTasksByProjectId(projectId, status, assigneeId)

            domainTasks.map { it.toService() }

        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for project $projectId", e)
            throw e
        }
    }
}