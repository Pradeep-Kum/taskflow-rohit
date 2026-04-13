package com.taskflow.tasks.data.queries

import com.taskflow.tasks.data.mappers.TaskQueryMapper
import com.taskflow.tasks.data.repos.TaskPsqlRepo
import com.taskflow.tasks.data.tables.TaskProjectTable
import com.taskflow.tasks.data.tables.TaskTable
import com.taskflow.tasks.domain.entities.CreateTaskDraft
import com.taskflow.tasks.domain.entities.TaskPriority
import com.taskflow.tasks.domain.entities.TaskStatus
import com.taskflow.tasks.domain.entities.UpdateTaskDraft
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TaskQueriesTest {
    private val ownerId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val creatorId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val assigneeId = UUID.fromString("33333333-3333-3333-3333-333333333333")
    private val otherUserId = UUID.fromString("44444444-4444-4444-4444-444444444444")
    private val projectId = UUID.fromString("55555555-5555-5555-5555-555555555555")
    private val taskId = UUID.fromString("66666666-6666-6666-6666-666666666666")
    private val now = OffsetDateTime.parse("2026-04-13T10:00:00Z")

    private lateinit var db: Database
    private lateinit var queries: TaskQueries

    @BeforeTest
    fun setUp() {
        db = Database.connect(
            url = "jdbc:h2:mem:tasks-${UUID.randomUUID()};MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction(db) {
            SchemaUtils.create(TaskProjectTable, TaskTable)
        }
        queries = TaskQueries(db, TaskQueryMapper())
    }

    @Test
    fun `insert task creates task from draft`() {
        seedProject(projectId, ownerId)
        val dueDate = now.plusDays(2)
        val repo = TaskPsqlRepo(queries)

        val created = repo.createTask(
            CreateTaskDraft(
                title = "Design",
                description = "Homepage",
                status = TaskStatus.TODO,
                priority = TaskPriority.HIGH,
                projectId = projectId,
                assigneeId = assigneeId,
                createdById = creatorId,
                dueDate = dueDate
            )
        )

        assertEquals("Design", created.title)
        assertEquals(TaskStatus.TODO, created.status)
        assertEquals(TaskPriority.HIGH, created.priority)
        assertEquals(assigneeId, created.assigneeId)
        assertEquals(dueDate, created.dueDate)
    }

    @Test
    fun `find tasks applies status and assignee filters`() {
        seedProject(projectId, ownerId)
        seedTask(taskId, projectId, creatorId, assigneeId, "todo", "high")
        seedTask(UUID.randomUUID(), projectId, creatorId, null, "done", "low")
        seedTask(UUID.randomUUID(), UUID.randomUUID(), creatorId, assigneeId, "todo", "medium")

        val filtered = queries.findTasksByProjectId(projectId, TaskStatus.TODO, assigneeId)

        assertEquals(listOf(taskId), filtered.map { it.id })
        assertEquals(TaskStatus.TODO, filtered.single().status)
        assertEquals(TaskPriority.HIGH, filtered.single().priority)
    }

    @Test
    fun `update task updates provided fields and maps result`() {
        seedProject(projectId, ownerId)
        seedTask(taskId, projectId, creatorId, null, "todo", "high")
        val dueDate = now.plusDays(5)

        val updated = queries.updateTask(
            taskId,
            UpdateTaskDraft(
                title = "Build",
                description = "API",
                status = TaskStatus.DONE,
                priority = TaskPriority.LOW,
                assigneeId = assigneeId,
                dueDate = dueDate
            )
        )

        assertEquals("Build", updated?.title)
        assertEquals("API", updated?.description)
        assertEquals(TaskStatus.DONE, updated?.status)
        assertEquals(TaskPriority.LOW, updated?.priority)
        assertEquals(assigneeId, updated?.assigneeId)
        assertEquals(dueDate, updated?.dueDate)
        assertNull(queries.updateTask(UUID.randomUUID(), UpdateTaskDraft(title = "Missing")))
    }

    @Test
    fun `delete task returns state for owner creator forbidden and missing`() {
        seedProject(projectId, ownerId)
        seedTask(taskId, projectId, creatorId, assigneeId, "todo", "high")
        val creatorTaskId = UUID.randomUUID()
        seedTask(creatorTaskId, projectId, creatorId, null, "done", "low")
        val forbiddenTaskId = UUID.randomUUID()
        seedTask(forbiddenTaskId, projectId, creatorId, null, "todo", "medium")

        assertTrue(queries.deleteTask(taskId, ownerId) == true)
        assertTrue(queries.deleteTask(creatorTaskId, creatorId) == true)
        assertFalse(queries.deleteTask(forbiddenTaskId, otherUserId) == true)
        assertNull(queries.deleteTask(UUID.randomUUID(), ownerId))
        assertEquals(1, transaction(db) { TaskTable.selectAll().where { TaskTable.id eq forbiddenTaskId }.count() })
    }

    private fun seedProject(id: UUID, owner: UUID) {
        transaction(db) {
            TaskProjectTable.insert {
                it[TaskProjectTable.id] = id
                it[name] = "Project"
                it[description] = "Desc"
                it[ownerId] = owner
                it[createdAt] = now
            }
        }
    }

    private fun seedTask(
        id: UUID,
        project: UUID,
        creator: UUID,
        assignee: UUID?,
        status: String,
        priority: String
    ) {
        transaction(db) {
            TaskTable.insert {
                it[TaskTable.id] = id
                it[title] = "Task-$id"
                it[description] = "Desc"
                it[TaskTable.status] = status
                it[TaskTable.priority] = priority
                it[projectId] = project
                it[assigneeId] = assignee
                it[createdById] = creator
                it[dueDate] = null
                it[createdAt] = now
                it[updatedAt] = now
            }
        }
    }
}
