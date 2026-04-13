package com.taskflow.tasks.domain.usecases

import com.taskflow.tasks.domain.entities.CreateTaskDraft
import com.taskflow.tasks.domain.entities.CreateTaskRequest
import com.taskflow.tasks.domain.entities.Task
import com.taskflow.tasks.domain.entities.TaskPriority
import com.taskflow.tasks.domain.entities.TaskStatus
import com.taskflow.tasks.domain.entities.UpdateTaskDraft
import com.taskflow.tasks.domain.entities.UpdateTaskRequest
import com.taskflow.tasks.domain.repos.TaskRepo
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TaskUseCasesTest {
    private val taskId = UUID.fromString("33333333-3333-3333-3333-333333333333")
    private val projectId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val userId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val assigneeId = UUID.fromString("44444444-4444-4444-4444-444444444444")
    private val now = OffsetDateTime.parse("2026-04-13T10:00:00Z")

    @Test
    fun `create task builds draft with default todo status parsed priority and due date`() {
        val repo = FakeTaskRepo(task())

        CreateTask(repo).create(
            projectId,
            userId,
            CreateTaskRequest("Design", "Homepage", "high", assigneeId.toString(), "2026-04-15")
        )

        val draft = repo.createdDraft!!
        assertEquals("Design", draft.title)
        assertEquals(TaskStatus.TODO, draft.status)
        assertEquals(TaskPriority.HIGH, draft.priority)
        assertEquals(projectId, draft.projectId)
        assertEquals(userId, draft.createdById)
        assertEquals(assigneeId, draft.assigneeId)
        assertEquals(OffsetDateTime.parse("2026-04-15T00:00:00Z"), draft.dueDate)
    }

    @Test
    fun `get tasks passes filters to repository`() {
        val repo = FakeTaskRepo(task())

        GetTask(repo).getTasksByProjectId(projectId, TaskStatus.IN_PROGRESS, assigneeId)

        assertEquals(projectId, repo.lastProjectId)
        assertEquals(TaskStatus.IN_PROGRESS, repo.lastStatus)
        assertEquals(assigneeId, repo.lastAssigneeId)
    }

    @Test
    fun `update task builds draft and returns updated task`() {
        val repo = FakeTaskRepo(task(status = TaskStatus.DONE))

        val updated = UpdateTask(repo).update(
            taskId,
            UpdateTaskRequest(
                title = "Build",
                description = "API",
                status = TaskStatus.DONE,
                priority = TaskPriority.LOW,
                assigneeId = assigneeId.toString(),
                dueDate = "2026-04-20"
            )
        )

        val draft = repo.updatedDraft!!
        assertEquals("Build", draft.title)
        assertEquals(TaskStatus.DONE, draft.status)
        assertEquals(TaskPriority.LOW, draft.priority)
        assertEquals(assigneeId, draft.assigneeId)
        assertEquals(OffsetDateTime.parse("2026-04-20T00:00:00Z"), draft.dueDate)
        assertEquals(TaskStatus.DONE, updated.status)
    }

    @Test
    fun `update task throws when repository returns null`() {
        assertFailsWith<NoSuchElementException> {
            UpdateTask(FakeTaskRepo(updateResult = null)).update(taskId, UpdateTaskRequest(title = "Missing"))
        }
    }

    @Test
    fun `delete task maps repository states`() {
        DeleteTask(FakeTaskRepo(deleteResult = true)).delete(taskId, userId)

        assertFailsWith<SecurityException> {
            DeleteTask(FakeTaskRepo(deleteResult = false)).delete(taskId, userId)
        }

        assertFailsWith<NoSuchElementException> {
            DeleteTask(FakeTaskRepo(deleteResult = null)).delete(taskId, userId)
        }
    }

    private fun task(status: TaskStatus = TaskStatus.TODO) = Task(
        id = taskId,
        title = "Design",
        description = "Homepage",
        status = status,
        priority = TaskPriority.HIGH,
        projectId = projectId,
        assigneeId = assigneeId,
        dueDate = null,
        createdAt = now,
        updatedAt = now
    )

    private class FakeTaskRepo(
        private val task: Task = Task(
            UUID.fromString("33333333-3333-3333-3333-333333333333"),
            "Design",
            "Homepage",
            TaskStatus.TODO,
            TaskPriority.HIGH,
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            null,
            null,
            OffsetDateTime.parse("2026-04-13T10:00:00Z"),
            OffsetDateTime.parse("2026-04-13T10:00:00Z")
        ),
        private val updateResult: Task? = task,
        private val deleteResult: Boolean? = true
    ) : TaskRepo {
        var createdDraft: CreateTaskDraft? = null
        var updatedDraft: UpdateTaskDraft? = null
        var lastProjectId: UUID? = null
        var lastStatus: TaskStatus? = null
        var lastAssigneeId: UUID? = null

        override fun getAllTasks(projectId: UUID, status: TaskStatus?, assigneeId: UUID?): List<Task> {
            lastProjectId = projectId
            lastStatus = status
            lastAssigneeId = assigneeId
            return listOf(task)
        }

        override fun createTask(draft: CreateTaskDraft): Task {
            createdDraft = draft
            return task.copy(
                title = draft.title,
                description = draft.description,
                status = draft.status,
                priority = draft.priority,
                projectId = draft.projectId,
                assigneeId = draft.assigneeId,
                dueDate = draft.dueDate
            )
        }

        override fun updateTask(taskId: UUID, draft: UpdateTaskDraft): Task? {
            updatedDraft = draft
            return updateResult
        }

        override fun deleteTask(taskId: UUID, userId: UUID): Boolean? = deleteResult
    }
}
