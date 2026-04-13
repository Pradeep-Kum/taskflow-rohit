package com.taskflow.projects.domain.usecases

import com.taskflow.projects.domain.entities.ProjectDetails
import com.taskflow.projects.domain.repos.ProjectRepo
import java.util.UUID

class GetProject(
    private val repo: ProjectRepo
) {
    fun getProjectDetails(projectId: UUID, userId: UUID): ProjectDetails {
        return repo.getProjectDetails(projectId, userId)
            ?: throw NoSuchElementException("Project not found")
    }
}
