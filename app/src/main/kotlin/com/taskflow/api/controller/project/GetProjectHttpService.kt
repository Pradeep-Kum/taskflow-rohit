package com.taskflow.api.controller.project

import com.taskflow.projects.service.usecases.GetProjectService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory
import java.util.UUID

class GetProjectHttpService(
    private val getProjectService: GetProjectService
) {
    private val logger = LoggerFactory.getLogger(GetProjectHttpService::class.java)

    suspend fun handleGetProject(call: ApplicationCall) {
        val projectIdStr = call.parameters["id"]

        try {
            if (projectIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Project ID is required"))
                return
            }

            val projectId = UUID.fromString(projectIdStr)
            val userId = call.requireUserId()
            val project = getProjectService.getProjectDetails(projectId, userId)
            call.respond(HttpStatusCode.OK, project)
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
        } catch (e: NoSuchElementException) {
            logger.warn("Project not found: $projectIdStr")
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "not found"))
        } catch (e: Exception) {
            logger.error("Failed to fetch project $projectIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
