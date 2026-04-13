package com.taskflow.tasks.data.repos

import com.taskflow.tasks.data.queries.TaskQueries
import com.taskflow.tasks.domain.entities.CreateTaskDraft
import com.taskflow.tasks.domain.entities.Task
import com.taskflow.tasks.domain.entities.TaskStatus
import com.taskflow.tasks.domain.entities.UpdateTaskDraft
import com.taskflow.tasks.domain.repos.TaskRepo
import java.util.*

class TaskPsqlRepo(
    val queries: TaskQueries
): TaskRepo {
    override fun getAllTasks(projectId: UUID, status: TaskStatus?, assigneeId: UUID?): List<Task> {
        return queries.findTasksByProjectId(projectId, status, assigneeId)
    }

    override fun createTask(draft: CreateTaskDraft): Task {
        return queries.insertTask(draft)
    }

    override fun updateTask(taskId: UUID, draft: UpdateTaskDraft): Task? {
        return queries.updateTask(taskId, draft)
    }

    override fun deleteTask(taskId: UUID, userId: UUID): Boolean? {
        return queries.deleteTask(taskId, userId)
    }
}
