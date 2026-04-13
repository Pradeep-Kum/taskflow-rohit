package com.taskflow.projects.service.usecases

import com.taskflow.projects.domain.entities.CreateProjectDraft
import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.entities.ProjectDetails
import com.taskflow.projects.domain.entities.ProjectTask
import com.taskflow.projects.domain.entities.UpdateProjectDraft
import com.taskflow.projects.domain.repos.ProjectRepo
import com.taskflow.projects.domain.usecases.CreateProject
import com.taskflow.projects.domain.usecases.DeleteProject
import com.taskflow.projects.domain.usecases.GetProject
import com.taskflow.projects.domain.usecases.GetProjects
import com.taskflow.projects.domain.usecases.UpdateProject
import com.taskflow.projects.service.entities.CreateProjectRequest
import com.taskflow.projects.service.entities.UpdateProjectRequest
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProjectServicesTest {
    private val projectId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val ownerId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val taskId = UUID.fromString("33333333-3333-3333-3333-333333333333")
    private val now = OffsetDateTime.parse("2026-04-13T10:00:00Z")

    @Test
    fun `create project service maps request and response`() {
        val repo = FakeProjectRepo(project())

        val created = CreateProjectService(CreateProject(repo))
            .createProject(ownerId, CreateProjectRequest("  Website  ", "  Redesign  "))

        assertEquals("Website", repo.createdDraft?.name)
        assertEquals("Redesign", repo.createdDraft?.description)
        assertEquals("Website", created.name)
        assertEquals(ownerId, created.ownerId)
    }

    @Test
    fun `get projects service maps project list`() {
        val projects = GetProjectsService(GetProjects(FakeProjectRepo(project())))
            .getProjects(ownerId)

        assertEquals(1, projects.size)
        assertEquals(projectId, projects.first().id)
        assertEquals("Website", projects.first().name)
    }

    @Test
    fun `get project service maps details and tasks`() {
        val details = GetProjectService(GetProject(FakeProjectRepo(project())))
            .getProjectDetails(projectId, ownerId)

        assertEquals(projectId, details.project.id)
        assertEquals(taskId, details.tasks.single().id)
        assertEquals("in_progress", details.tasks.single().status)
    }

    @Test
    fun `update project service maps request and response`() {
        val repo = FakeProjectRepo(project(name = "Updated"))

        val updated = UpdateProjectService(UpdateProject(repo))
            .updateProject(projectId, ownerId, UpdateProjectRequest(" Updated ", " Done "))

        assertEquals("Updated", repo.updatedDraft?.name)
        assertEquals("Done", repo.updatedDraft?.description)
        assertEquals("Updated", updated.name)
    }

    @Test
    fun `delete project service rethrows forbidden failures`() {
        val service = DeleteProjectService(DeleteProject(FakeProjectRepo(project(), owner = false)))

        assertFailsWith<SecurityException> {
            service.deleteProject(projectId, ownerId)
        }
    }

    private fun project(name: String = "Website") = Project(projectId, name, "Desc", ownerId, now)

    private fun projectTask() = ProjectTask(
        id = taskId,
        title = "Design",
        description = "Homepage",
        status = "in_progress",
        priority = "high",
        assigneeId = ownerId,
        dueDate = now.plusDays(1),
        createdAt = now,
        updatedAt = now
    )

    private class FakeProjectRepo(
        private val project: Project,
        private val owner: Boolean = true
    ) : ProjectRepo {
        var createdDraft: CreateProjectDraft? = null
        var updatedDraft: UpdateProjectDraft? = null

        override fun getAccessibleProjects(userId: UUID): List<Project> = listOf(project)
        override fun getProjectDetails(projectId: UUID, userId: UUID): ProjectDetails = ProjectDetails(
            project = project,
            tasks = listOf(
                ProjectTask(
                    id = UUID.fromString("33333333-3333-3333-3333-333333333333"),
                    title = "Design",
                    description = "Homepage",
                    status = "in_progress",
                    priority = "high",
                    assigneeId = project.ownerId,
                    dueDate = project.createdAt.plusDays(1),
                    createdAt = project.createdAt,
                    updatedAt = project.createdAt
                )
            )
        )

        override fun createProject(draft: CreateProjectDraft): Project {
            createdDraft = draft
            return project.copy(name = draft.name, description = draft.description, ownerId = draft.ownerId)
        }

        override fun updateProject(projectId: UUID, draft: UpdateProjectDraft): Project {
            updatedDraft = draft
            return project.copy(name = draft.name ?: project.name, description = draft.description)
        }

        override fun deleteProject(projectId: UUID): Boolean = true
        override fun isOwner(projectId: UUID, userId: UUID): Boolean = owner
    }
}
