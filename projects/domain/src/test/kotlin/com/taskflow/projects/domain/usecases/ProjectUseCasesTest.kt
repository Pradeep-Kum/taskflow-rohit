package com.taskflow.projects.domain.usecases

import com.taskflow.projects.domain.entities.CreateProjectDraft
import com.taskflow.projects.domain.entities.CreateProjectRequest
import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.entities.ProjectDetails
import com.taskflow.projects.domain.entities.UpdateProjectDraft
import com.taskflow.projects.domain.entities.UpdateProjectRequest
import com.taskflow.projects.domain.repos.ProjectRepo
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ProjectUseCasesTest {
    private val projectId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val ownerId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val now = OffsetDateTime.parse("2026-04-13T10:00:00Z")

    @Test
    fun `create project trims name and empty description`() {
        val repo = FakeProjectRepo(project = project())

        CreateProject(repo).createProject(ownerId, CreateProjectRequest("  Website  ", "   "))

        assertEquals("Website", repo.createdDraft?.name)
        assertNull(repo.createdDraft?.description)
        assertEquals(ownerId, repo.createdDraft?.ownerId)
    }

    @Test
    fun `create project rejects blank name`() {
        assertFailsWith<IllegalArgumentException> {
            CreateProject(FakeProjectRepo()).createProject(ownerId, CreateProjectRequest("   ", null))
        }
    }

    @Test
    fun `update project requires owner`() {
        assertFailsWith<SecurityException> {
            UpdateProject(FakeProjectRepo(owner = false)).updateProject(projectId, ownerId, UpdateProjectRequest("New", null))
        }
    }

    @Test
    fun `update project rejects blank provided name`() {
        assertFailsWith<IllegalArgumentException> {
            UpdateProject(FakeProjectRepo()).updateProject(projectId, ownerId, UpdateProjectRequest("   ", "Description"))
        }
    }

    @Test
    fun `update project passes trimmed draft to repository`() {
        val repo = FakeProjectRepo(project = project(name = "New"))

        val updated = UpdateProject(repo).updateProject(projectId, ownerId, UpdateProjectRequest("  New  ", "  Updated  "))

        assertEquals("New", repo.updatedDraft?.name)
        assertEquals("Updated", repo.updatedDraft?.description)
        assertEquals("New", updated.name)
    }

    @Test
    fun `delete project maps repository states to exceptions`() {
        assertFailsWith<SecurityException> {
            DeleteProject(FakeProjectRepo(owner = false)).deleteProject(projectId, ownerId)
        }

        assertFailsWith<NoSuchElementException> {
            DeleteProject(FakeProjectRepo(deleteResult = false)).deleteProject(projectId, ownerId)
        }
    }

    @Test
    fun `get project and list projects return repository results`() {
        val project = project()
        val repo = FakeProjectRepo(project = project)

        assertEquals(listOf(project), GetProjects(repo).getAccessibleProjects(ownerId))
        assertEquals(ProjectDetails(project, emptyList()), GetProject(repo).getProjectDetails(projectId, ownerId))
    }

    private fun project(name: String = "Website") = Project(projectId, name, "Desc", ownerId, now)

    private class FakeProjectRepo(
        private val project: Project = Project(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "Website",
            "Desc",
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            OffsetDateTime.parse("2026-04-13T10:00:00Z")
        ),
        private val owner: Boolean = true,
        private val deleteResult: Boolean = true
    ) : ProjectRepo {
        var createdDraft: CreateProjectDraft? = null
        var updatedDraft: UpdateProjectDraft? = null

        override fun getAccessibleProjects(userId: UUID): List<Project> = listOf(project)
        override fun getProjectDetails(projectId: UUID, userId: UUID): ProjectDetails? = ProjectDetails(project, emptyList())

        override fun createProject(draft: CreateProjectDraft): Project {
            createdDraft = draft
            return project.copy(name = draft.name, description = draft.description, ownerId = draft.ownerId)
        }

        override fun updateProject(projectId: UUID, draft: UpdateProjectDraft): Project? {
            updatedDraft = draft
            return project.copy(name = draft.name ?: project.name, description = draft.description)
        }

        override fun deleteProject(projectId: UUID): Boolean = deleteResult
        override fun isOwner(projectId: UUID, userId: UUID): Boolean = owner
    }
}
