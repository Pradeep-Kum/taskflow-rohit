package com.taskflow.auth.api.controller.task

import com.taskflow.api.controller.project.requireUserId
import com.taskflow.tasks.service.usecases.DeleteTaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import java.util.*

class DeleteTaskHttpService(
    private val deleteTaskService: DeleteTaskService
) {
    private val logger = LoggerFactory.getLogger(DeleteTaskHttpService::class.java)

    suspend fun handleDeleteTask(call: ApplicationCall) {
        val taskIdStr = call.parameters["taskId"]

        try {
            if (taskIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Task ID is required"))
                return
            }
            val taskId = UUID.fromString(taskIdStr)
            val userId = call.requireUserId()

            deleteTaskService.deleteTask(taskId, userId)

            call.respond(HttpStatusCode.NoContent)

        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid UUID format provided: $taskIdStr")
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid ID format"))
        } catch (e: NoSuchElementException) {
            logger.warn("Task not found: $taskIdStr")
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found"))
        } catch (e: SecurityException) {
            logger.warn("User is not allowed to delete task $taskIdStr")
            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "forbidden"))
        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for task $taskIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
