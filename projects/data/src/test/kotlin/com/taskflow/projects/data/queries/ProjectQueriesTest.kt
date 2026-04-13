package com.taskflow.projects.data.queries

import com.taskflow.projects.data.mappers.ProjectQueryMapper
import com.taskflow.projects.data.repos.ProjectPsqlRepo
import com.taskflow.projects.data.tables.ProjectTable
import com.taskflow.projects.data.tables.ProjectTaskTable
import com.taskflow.projects.domain.entities.CreateProjectDraft
import com.taskflow.projects.domain.entities.UpdateProjectDraft
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProjectQueriesTest {
    private val ownerId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val assigneeId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val otherUserId = UUID.fromString("33333333-3333-3333-3333-333333333333")
    private val projectId = UUID.fromString("44444444-4444-4444-4444-444444444444")
    private val assignedProjectId = UUID.fromString("55555555-5555-5555-5555-555555555555")
    private val taskId = UUID.fromString("66666666-6666-6666-6666-666666666666")
    private val now = OffsetDateTime.parse("2026-04-13T10:00:00Z")

    private lateinit var db: Database
    private lateinit var queries: ProjectQueries

    @BeforeTest
    fun setUp() {
        db = Database.connect(
            url = "jdbc:h2:mem:projects-${UUID.randomUUID()};MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction(db) {
            SchemaUtils.create(ProjectTable, ProjectTaskTable)
        }
        queries = ProjectQueries(db, ProjectQueryMapper())
    }

    @Test
    fun `insert project creates project from draft`() {
        val created = queries.insertProject(CreateProjectDraft("Website", "Redesign", ownerId))

        assertEquals("Website", created.name)
        assertEquals("Redesign", created.description)
        assertEquals(ownerId, created.ownerId)
        assertNotNull(created.id)
    }

    @Test
    fun `accessible projects includes owned and assigned projects only once`() {
        seedProject(projectId, ownerId)
        seedProject(assignedProjectId, otherUserId)
        seedTask(taskId, assignedProjectId, assigneeId)

        val ownerProjects = queries.findAccessibleProjects(ownerId)
        val assigneeProjects = queries.findAccessibleProjects(assigneeId)

        assertEquals(listOf(projectId), ownerProjects.map { it.id })
        assertEquals(listOf(assignedProjectId), assigneeProjects.map { it.id })
        assertEquals(emptyList(), queries.findAccessibleProjects(UUID.randomUUID()))
    }

    @Test
    fun `project details includes tasks when user can access project`() {
        seedProject(projectId, ownerId)
        seedTask(taskId, projectId, assigneeId)

        val details = queries.findProjectDetails(projectId, ownerId)

        assertEquals(projectId, details?.project?.id)
        assertEquals(taskId, details?.tasks?.single()?.id)
        assertEquals("in_progress", details?.tasks?.single()?.status)
        assertNull(queries.findProjectDetails(projectId, otherUserId))
    }

    @Test
    fun `update delete and owner checks use persisted project state`() {
        seedProject(projectId, ownerId, description = "Old")
        val repo = ProjectPsqlRepo(queries)

        val updated = repo.updateProject(projectId, UpdateProjectDraft("Updated", null))

        assertEquals("Updated", updated?.name)
        assertNull(updated?.description)
        assertTrue(repo.isOwner(projectId, ownerId))
        assertFalse(repo.isOwner(projectId, otherUserId))
        assertTrue(repo.deleteProject(projectId))
        assertFalse(repo.deleteProject(projectId))
    }

    private fun seedProject(id: UUID, owner: UUID, description: String? = "Desc") {
        transaction(db) {
            ProjectTable.insert {
                it[ProjectTable.id] = id
                it[name] = "Project-$id"
                it[ProjectTable.description] = description
                it[ownerId] = owner
                it[createdAt] = now
            }
        }
    }

    private fun seedTask(id: UUID, project: UUID, assignee: UUID?) {
        transaction(db) {
            ProjectTaskTable.insert {
                it[ProjectTaskTable.id] = id
                it[title] = "Design"
                it[description] = "Homepage"
                it[status] = "in_progress"
                it[priority] = "high"
                it[projectId] = project
                it[assigneeId] = assignee
                it[dueDate] = now.plusDays(1)
                it[createdAt] = now
                it[updatedAt] = now
            }
        }
    }
}
