package com.taskflow.projects.domain.usecases

import com.taskflow.projects.domain.repos.ProjectRepo
import java.util.UUID

class DeleteProject(
    private val repo: ProjectRepo
) {
    fun deleteProject(projectId: UUID, userId: UUID) {
        if (!repo.isOwner(projectId, userId)) {
            throw SecurityException("Only the project owner can delete this project")
        }

        val deleted = repo.deleteProject(projectId)
        if (!deleted) {
            throw NoSuchElementException("Project not found")
        }
    }
}
