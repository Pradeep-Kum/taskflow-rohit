package com.taskflow.auth.api.controller.task

import com.taskflow.tasks.service.usecases.DeleteTaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import java.util.*

class DeleteTaskHttpService(
    private val deleteTaskService: DeleteTaskService
) {
    private val logger = LoggerFactory.getLogger(_root_ide_package_.com.taskflow.auth.api.controller.task.DeleteTaskHttpService::class.java)

    suspend fun handleDeleteTask(call: ApplicationCall) {
        val taskIdStr = call.parameters["id"]

        try {
            if (taskIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, "task ID is required")
                return
            }
            val taskId = UUID.fromString(taskIdStr)

            val tasks = deleteTaskService.deleteTask(taskId)

            call.respond(HttpStatusCode.OK, tasks)

        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid UUID format provided: $taskIdStr")
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for task $taskIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
        }
    }
}