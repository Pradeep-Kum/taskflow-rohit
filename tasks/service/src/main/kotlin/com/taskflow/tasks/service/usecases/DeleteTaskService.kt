package com.taskflow.tasks.service.usecases

import com.taskflow.domain.usecases.DeleteTask
import org.slf4j.LoggerFactory
import java.util.*

class DeleteTaskService(
    private val deleteTask: DeleteTask
) {
    private val logger = LoggerFactory.getLogger(GetTaskService::class.java)

    fun deleteTask(taskId: UUID) {
        return try {
            logger.info("Fetching tasks for project: $taskId")

            deleteTask.delete(taskId)
        } catch (e: Exception) {
            logger.error("Failed to delete task for task $taskId", e)
            throw e
        }
    }
}