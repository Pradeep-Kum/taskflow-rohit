package com.taskflow.api.controller.project

import com.taskflow.projects.service.entities.CreateProjectRequest
import com.taskflow.projects.service.usecases.CreateProjectService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory

class CreateProjectHttpService(
    private val createProjectService: CreateProjectService
) {
    private val logger = LoggerFactory.getLogger(CreateProjectHttpService::class.java)

    suspend fun handleCreateProject(call: ApplicationCall) {
        try {
            val userId = call.requireUserId()
            val request = call.receive<CreateProjectRequest>()
            val createdProject = createProjectService.createProject(userId, request)
            call.respond(HttpStatusCode.Created, createdProject)
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
        } catch (e: SerializationException) {
            logger.warn("Malformed JSON body for project creation: ${e.message}")
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Malformed JSON body"))
        } catch (e: Exception) {
            logger.error("Failed to create project", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
