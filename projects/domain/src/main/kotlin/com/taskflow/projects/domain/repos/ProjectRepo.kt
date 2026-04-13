package com.taskflow.projects.domain.repos

import com.taskflow.projects.domain.entities.CreateProjectDraft
import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.entities.ProjectDetails
import com.taskflow.projects.domain.entities.UpdateProjectDraft
import java.util.UUID

interface ProjectRepo {
    fun getAccessibleProjects(userId: UUID): List<Project>

    fun getProjectDetails(projectId: UUID, userId: UUID): ProjectDetails?

    fun createProject(draft: CreateProjectDraft): Project

    fun updateProject(projectId: UUID, draft: UpdateProjectDraft): Project?

    fun deleteProject(projectId: UUID): Boolean

    fun isOwner(projectId: UUID, userId: UUID): Boolean
}
