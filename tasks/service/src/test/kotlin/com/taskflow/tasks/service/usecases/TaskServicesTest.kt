package com.taskflow.tasks.service.usecases

import com.taskflow.tasks.domain.entities.CreateTaskDraft
import com.taskflow.tasks.domain.entities.Task
import com.taskflow.tasks.domain.entities.TaskPriority
import com.taskflow.tasks.domain.entities.TaskStatus
import com.taskflow.tasks.domain.entities.UpdateTaskDraft
import com.taskflow.tasks.domain.repos.TaskRepo
import com.taskflow.tasks.domain.usecases.CreateTask
import com.taskflow.tasks.domain.usecases.DeleteTask
import com.taskflow.tasks.domain.usecases.GetTask
import com.taskflow.tasks.domain.usecases.UpdateTask
import com.taskflow.tasks.service.entities.CreateTaskRequest
import com.taskflow.tasks.service.entities.TaskPriority as ServiceTaskPriority
import com.taskflow.tasks.service.entities.TaskStatus as ServiceTaskStatus
import com.taskflow.tasks.service.entities.UpdateTaskRequest
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TaskServicesTest {
    private val taskId = UUID.fromString("33333333-3333-3333-3333-333333333333")
    private val projectId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val userId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val assigneeId = UUID.fromString("44444444-4444-4444-4444-444444444444")
    private val now = OffsetDateTime.parse("2026-04-13T10:00:00Z")

    @Test
    fun `create task service maps request and response`() {
        val repo = FakeTaskRepo(task())

        val created = CreateTaskService(CreateTask(repo)).createTask(
            projectId,
            userId,
            CreateTaskRequest("Design", "Homepage", "high", assigneeId.toString(), "2026-04-15")
        )

        assertEquals(TaskStatus.TODO, repo.createdDraft?.status)
        assertEquals(TaskPriority.HIGH, repo.createdDraft?.priority)
        assertEquals(ServiceTaskStatus.TODO, created.status)
        assertEquals(ServiceTaskPriority.HIGH, created.priority)
    }

    @Test
    fun `get task service parses status filter and maps response`() {
        val repo = FakeTaskRepo(task(status = TaskStatus.IN_PROGRESS))

        val tasks = GetTaskService(GetTask(repo)).getProjectTasks(projectId, "in_progress", assigneeId)

        assertEquals(TaskStatus.IN_PROGRESS, repo.lastStatus)
        assertEquals(assigneeId, repo.lastAssigneeId)
        assertEquals(ServiceTaskStatus.IN_PROGRESS, tasks.single().status)
    }

    @Test
    fun `update task service maps enum request and response`() {
        val repo = FakeTaskRepo(task(status = TaskStatus.DONE, priority = TaskPriority.LOW))

        val updated = UpdateTaskService(UpdateTask(repo)).updateTask(
            taskId,
            UpdateTaskRequest(
                title = "Build",
                status = ServiceTaskStatus.DONE,
                priority = ServiceTaskPriority.LOW,
                assigneeId = assigneeId.toString(),
                dueDate = "2026-04-20"
            )
        )

        assertEquals(TaskStatus.DONE, repo.updatedDraft?.status)
        assertEquals(TaskPriority.LOW, repo.updatedDraft?.priority)
        assertEquals(ServiceTaskStatus.DONE, updated.status)
        assertEquals(ServiceTaskPriority.LOW, updated.priority)
    }

    @Test
    fun `delete task service rethrows forbidden failures`() {
        val service = DeleteTaskService(DeleteTask(FakeTaskRepo(deleteResult = false)))

        assertFailsWith<SecurityException> {
            service.deleteTask(taskId, userId)
        }
    }

    private fun task(
        status: TaskStatus = TaskStatus.TODO,
        priority: TaskPriority = TaskPriority.HIGH
    ) = Task(
        id = taskId,
        title = "Design",
        description = "Homepage",
        status = status,
        priority = priority,
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
        private val deleteResult: Boolean? = true
    ) : TaskRepo {
        var createdDraft: CreateTaskDraft? = null
        var updatedDraft: UpdateTaskDraft? = null
        var lastStatus: TaskStatus? = null
        var lastAssigneeId: UUID? = null

        override fun getAllTasks(projectId: UUID, status: TaskStatus?, assigneeId: UUID?): List<Task> {
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
            return task
        }

        override fun deleteTask(taskId: UUID, userId: UUID): Boolean? = deleteResult
    }
}
