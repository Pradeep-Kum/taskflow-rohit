package com.taskflow.projects.service.usecases

import com.taskflow.projects.service.entities.Project
import com.taskflow.projects.service.mappers.toService
import com.taskflow.projects.domain.usecases.GetProjects
import org.slf4j.LoggerFactory
import java.util.UUID

class GetProjectsService(
    private val getProjects: GetProjects
) {
    private val logger = LoggerFactory.getLogger(GetProjectsService::class.java)

    fun getProjects(userId: UUID): List<Project> {
        return try {
            logger.info("Fetching accessible projects for user: $userId")
            getProjects.getAccessibleProjects(userId).map { it.toService() }
        } catch (e: Exception) {
            logger.error("Failed to fetch projects for user $userId", e)
            throw e
        }
    }
}
