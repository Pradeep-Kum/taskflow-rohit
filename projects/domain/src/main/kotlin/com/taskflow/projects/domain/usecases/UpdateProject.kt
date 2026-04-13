package com.taskflow.projects.domain.usecases

import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.entities.UpdateProjectDraft
import com.taskflow.projects.domain.entities.UpdateProjectRequest
import com.taskflow.projects.domain.repos.ProjectRepo
import java.util.UUID

class UpdateProject(
    private val repo: ProjectRepo
) {
    fun updateProject(projectId: UUID, userId: UUID, request: UpdateProjectRequest): Project {
        if (!repo.isOwner(projectId, userId)) {
            throw SecurityException("Only the project owner can update this project")
        }

        val draft = UpdateProjectDraft(
            name = request.name?.trim()?.takeIf { it.isNotEmpty() },
            description = request.description?.trim()
        )

        if (draft.name == null && request.name != null) {
            throw IllegalArgumentException("Project name cannot be blank")
        }

        return repo.updateProject(projectId, draft)
            ?: throw NoSuchElementException("Project not found")
    }
}
