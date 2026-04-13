package com.taskflow.projects.domain.usecases

import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.repos.ProjectRepo
import java.util.UUID

class GetProjects(
    private val repo: ProjectRepo
) {
    fun getAccessibleProjects(userId: UUID): List<Project> {
        return repo.getAccessibleProjects(userId)
    }
}
