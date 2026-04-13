package com.taskflow.tasks.data.repos

import com.taskflow.domain.entities.Task
import com.taskflow.domain.entities.CreateTaskDraft
import com.taskflow.domain.entities.TaskStatus
import com.taskflow.domain.entities.UpdateTaskDraft
import com.taskflow.domain.repos.TaskRepo
import com.taskflow.queries.TaskQueries
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

    override fun deleteTask(taskId: UUID) {
        return queries.deleteTask(taskId)
    }
}