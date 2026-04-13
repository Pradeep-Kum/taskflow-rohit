package com.taskflow.auth.api.controller.task

import com.taskflow.api.controller.project.requireUserId
import com.taskflow.tasks.service.entities.CreateTaskRequest
import com.taskflow.tasks.service.usecases.CreateTaskService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory
import java.util.UUID

class CreateTaskHttpService(
    private val createTaskService: CreateTaskService
) {
    private val logger = LoggerFactory.getLogger(GetTaskHttpService::class.java)

    suspend fun handleCreateTask(call: ApplicationCall) {
        val projectIdStr = call.parameters["id"]

        try {
            if (projectIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Project ID is required"))
                return
            }
            val projectId = UUID.fromString(projectIdStr)
            val userId = call.requireUserId()

            val request = call.receive<CreateTaskRequest>()

            val createdTask = createTaskService.createTask(projectId, userId, request)

            call.respond(HttpStatusCode.Created, createdTask)

        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
        } catch (e: SerializationException) {
            logger.warn("Malformed JSON body for project ID : ${e.message}")
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Malformed JSON body"))
        } catch (e: Exception) {
            logger.error("Failed to create task for project $projectIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
