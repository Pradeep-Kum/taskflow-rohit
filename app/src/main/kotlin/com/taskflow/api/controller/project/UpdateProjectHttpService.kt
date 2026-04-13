package com.taskflow.api.controller.project

import com.taskflow.projects.service.entities.UpdateProjectRequest
import com.taskflow.projects.service.usecases.UpdateProjectService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory
import java.util.UUID

class UpdateProjectHttpService(
    private val updateProjectService: UpdateProjectService
) {
    private val logger = LoggerFactory.getLogger(UpdateProjectHttpService::class.java)

    suspend fun handleUpdateProject(call: ApplicationCall) {
        val projectIdStr = call.parameters["id"]

        try {
            if (projectIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Project ID is required"))
                return
            }

            val projectId = UUID.fromString(projectIdStr)
            val userId = call.requireUserId()
            val request = call.receive<UpdateProjectRequest>()
            val updatedProject = updateProjectService.updateProject(projectId, userId, request)

            call.respond(HttpStatusCode.OK, updatedProject)
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
        } catch (e: SecurityException) {
            logger.warn("User is not allowed to update project $projectIdStr")
            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "forbidden"))
        } catch (e: NoSuchElementException) {
            logger.warn("Project not found: $projectIdStr")
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found"))
        } catch (e: SerializationException) {
            logger.warn("Malformed JSON body for project update: ${e.message}")
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Malformed JSON body"))
        } catch (e: Exception) {
            logger.error("Failed to update project $projectIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
