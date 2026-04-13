package com.taskflow.api.controller.project

import com.taskflow.projects.service.usecases.DeleteProjectService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory
import java.util.UUID

class DeleteProjectHttpService(
    private val deleteProjectService: DeleteProjectService
) {
    private val logger = LoggerFactory.getLogger(DeleteProjectHttpService::class.java)

    suspend fun handleDeleteProject(call: ApplicationCall) {
        val projectIdStr = call.parameters["id"]

        try {
            if (projectIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Project ID is required"))
                return
            }

            val projectId = UUID.fromString(projectIdStr)
            val userId = call.requireUserId()
            deleteProjectService.deleteProject(projectId, userId)

            call.respond(HttpStatusCode.NoContent)
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
        } catch (e: SecurityException) {
            logger.warn("User is not allowed to delete project $projectIdStr")
            call.respond(HttpStatusCode.Forbidden, mapOf("error" to "forbidden"))
        } catch (e: NoSuchElementException) {
            logger.warn("Project not found: $projectIdStr")
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found"))
        } catch (e: Exception) {
            logger.error("Failed to delete project $projectIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
