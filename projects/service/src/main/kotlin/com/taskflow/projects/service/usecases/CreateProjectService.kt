package com.taskflow.projects.service.usecases

import com.taskflow.projects.service.entities.CreateProjectRequest
import com.taskflow.projects.service.entities.Project
import com.taskflow.projects.service.mappers.toDomain
import com.taskflow.projects.service.mappers.toService
import com.taskflow.projects.domain.usecases.CreateProject
import org.slf4j.LoggerFactory
import java.util.UUID

class CreateProjectService(
    private val createProject: CreateProject
) {
    private val logger = LoggerFactory.getLogger(CreateProjectService::class.java)

    fun createProject(userId: UUID, request: CreateProjectRequest): Project {
        return try {
            logger.info("Creating project for user: $userId")
            createProject.createProject(userId, request.toDomain()).toService()
        } catch (e: Exception) {
            logger.error("Failed to create project for user $userId", e)
            throw e
        }
    }
}
