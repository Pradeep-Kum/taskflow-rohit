package com.taskflow.api.controller.project

import com.taskflow.projects.service.usecases.GetProjectsService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

class GetProjectsHttpService(
    private val getProjectsService: GetProjectsService
) {
    private val logger = LoggerFactory.getLogger(GetProjectsHttpService::class.java)

    suspend fun handleGetProjects(call: ApplicationCall) {
        try {
            val userId = call.requireUserId()
            val projects = getProjectsService.getProjects(userId)
            call.respond(HttpStatusCode.OK, projects)
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
        } catch (e: Exception) {
            logger.error("Failed to fetch projects", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An unexpected error occurred"))
        }
    }
}
