package com.taskflow.projects.service.usecases

import com.taskflow.projects.service.entities.ProjectDetails
import com.taskflow.projects.service.mappers.toService
import com.taskflow.projects.domain.usecases.GetProject
import org.slf4j.LoggerFactory
import java.util.UUID

class GetProjectService(
    private val getProject: GetProject
) {
    private val logger = LoggerFactory.getLogger(GetProjectService::class.java)

    fun getProjectDetails(projectId: UUID, userId: UUID): ProjectDetails {
        return try {
            logger.info("Fetching project details for project: $projectId and user: $userId")
            getProject.getProjectDetails(projectId, userId).toService()
        } catch (e: Exception) {
            logger.error("Failed to fetch project $projectId for user $userId", e)
            throw e
        }
    }
}
