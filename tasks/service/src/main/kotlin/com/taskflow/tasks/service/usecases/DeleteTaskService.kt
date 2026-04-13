package com.taskflow.tasks.service.usecases

import com.taskflow.tasks.domain.usecases.DeleteTask
import org.slf4j.LoggerFactory
import java.util.*

class DeleteTaskService(
    private val deleteTask: DeleteTask
) {
    private val logger = LoggerFactory.getLogger(GetTaskService::class.java)

    fun deleteTask(taskId: UUID, userId: UUID) {
        return try {
            logger.info("Deleting task: $taskId")
            deleteTask.delete(taskId, userId)
        } catch (e: Exception) {
            logger.error("Failed to delete task for task $taskId", e)
            throw e
        }
    }
}
