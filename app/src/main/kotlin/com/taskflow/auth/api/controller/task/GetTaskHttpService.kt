package com.taskflow.auth.api.controller.task

import com.taskflow.tasks.service.usecases.GetTaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import java.util.*

class GetTaskHttpService(
    private val getTaskService: GetTaskService
) {
    private val logger = LoggerFactory.getLogger(GetTaskHttpService::class.java)

    suspend fun getProjectTasks(call: ApplicationCall) {
        val projectIdStr = call.parameters["id"]

        try {
            if (projectIdStr == null) {
                call.respond(HttpStatusCode.BadRequest, "Project ID is required")
                return
            }
            val projectId = UUID.fromString(projectIdStr)

            val status = call.request.queryParameters["status"]

            val assigneeStr = call.request.queryParameters["assignee"]
            val assigneeId = assigneeStr?.let { UUID.fromString(it) }

            val tasks = getTaskService.getProjectTasks(projectId, status, assigneeId)

            call.respond(HttpStatusCode.OK, tasks)

        } catch (e: IllegalArgumentException) {
            logger.warn("Invalid UUID format provided: $projectIdStr")
            call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
        } catch (e: Exception) {
            logger.error("Failed to fetch tasks for project $projectIdStr", e)
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
        }
    }
}