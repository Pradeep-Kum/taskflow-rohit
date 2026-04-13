package com.taskflow.projects.data.repos

import com.taskflow.projects.data.queries.ProjectQueries
import com.taskflow.projects.domain.entities.CreateProjectDraft
import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.entities.ProjectDetails
import com.taskflow.projects.domain.entities.UpdateProjectDraft
import com.taskflow.projects.domain.repos.ProjectRepo
import java.util.UUID

class ProjectPsqlRepo(
    private val queries: ProjectQueries
) : ProjectRepo {
    override fun getAccessibleProjects(userId: UUID): List<Project> {
        return queries.findAccessibleProjects(userId)
    }

    override fun getProjectDetails(projectId: UUID, userId: UUID): ProjectDetails? {
        return queries.findProjectDetails(projectId, userId)
    }

    override fun createProject(draft: CreateProjectDraft): Project {
        return queries.insertProject(draft)
    }

    override fun updateProject(projectId: UUID, draft: UpdateProjectDraft): Project? {
        return queries.updateProject(projectId, draft)
    }

    override fun deleteProject(projectId: UUID): Boolean {
        return queries.deleteProject(projectId)
    }

    override fun isOwner(projectId: UUID, userId: UUID): Boolean {
        return queries.isOwner(projectId, userId)
    }
}
