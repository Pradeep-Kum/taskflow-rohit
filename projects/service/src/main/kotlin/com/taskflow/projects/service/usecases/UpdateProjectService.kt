package com.taskflow.projects.service.usecases

import com.taskflow.projects.service.entities.Project
import com.taskflow.projects.service.entities.UpdateProjectRequest
import com.taskflow.projects.service.mappers.toDomain
import com.taskflow.projects.service.mappers.toService
import com.taskflow.projects.domain.usecases.UpdateProject
import org.slf4j.LoggerFactory
import java.util.UUID

class UpdateProjectService(
    private val updateProject: UpdateProject
) {
    private val logger = LoggerFactory.getLogger(UpdateProjectService::class.java)

    fun updateProject(projectId: UUID, userId: UUID, request: UpdateProjectRequest): Project {
        return try {
            logger.info("Updating project $projectId for user: $userId")
            updateProject.updateProject(projectId, userId, request.toDomain()).toService()
        } catch (e: Exception) {
            logger.error("Failed to update project $projectId for user $userId", e)
            throw e
        }
    }
}
