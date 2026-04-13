package com.taskflow.projects.domain.usecases

import com.taskflow.projects.domain.entities.CreateProjectDraft
import com.taskflow.projects.domain.entities.CreateProjectRequest
import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.repos.ProjectRepo
import java.util.UUID

class CreateProject(
    private val repo: ProjectRepo
) {
    fun createProject(ownerId: UUID, request: CreateProjectRequest): Project {
        val name = request.name.trim()
        require(name.isNotBlank()) { "Project name is required" }

        return repo.createProject(
            CreateProjectDraft(
                name = name,
                description = request.description?.trim()?.takeIf { it.isNotEmpty() },
                ownerId = ownerId
            )
        )
    }
}
