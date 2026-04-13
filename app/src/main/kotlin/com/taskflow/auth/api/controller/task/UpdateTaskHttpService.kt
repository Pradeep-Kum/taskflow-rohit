package com.taskflow.auth.api.controller.task

import com.taskflow.tasks.service.entities.UpdateTaskRequest
import com.taskflow.tasks.service.usecases.UpdateTaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory
import java.util.*

class UpdateTaskHttpService(
    private val updateTaskService: UpdateTaskService
) {
    private val logger = LoggerFactory.getLogger(GetTaskHttpService::class.java)

    suspend fun handleUpdateTask(call: ApplicationCall) {
        val taskIdStr = call.parameters["taskId"]

        try {
            if (taskIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Task ID is required"))
                return
            }
            val taskId = UUID.fromString(taskIdStr)

            val request = call.receive<UpdateTaskRequest>()

            val updatedTask = updateTaskService.updateTask(taskId, request)

            call.respond(HttpStatusCode.OK, updatedTask)

        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
        } catch (e: SerializationException) {
            logger.warn("Malformed JSON body for project ID : ${e.message}")
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Malformed JSON body"))
        } catch (e: NoSuchElementException) {
            logger.warn("Task not found: $taskIdStr")
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found"))
        } catch (e: Exception) {
            logger.error("Failed to update task for project $taskIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
