package com.taskflow.projects.service.usecases

import com.taskflow.projects.domain.usecases.DeleteProject
import org.slf4j.LoggerFactory
import java.util.UUID

class DeleteProjectService(
    private val deleteProject: DeleteProject
) {
    private val logger = LoggerFactory.getLogger(DeleteProjectService::class.java)

    fun deleteProject(projectId: UUID, userId: UUID) {
        try {
            logger.info("Deleting project $projectId for user: $userId")
            deleteProject.deleteProject(projectId, userId)
        } catch (e: Exception) {
            logger.error("Failed to delete project $projectId for user $userId", e)
            throw e
        }
    }
}
