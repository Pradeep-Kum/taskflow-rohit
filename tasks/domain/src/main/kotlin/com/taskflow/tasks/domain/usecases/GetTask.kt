package com.taskflow.tasks.domain.usecases

import com.taskflow.tasks.domain.entities.Task
import com.taskflow.tasks.domain.entities.TaskStatus
import com.taskflow.tasks.domain.repos.TaskRepo
import java.util.UUID

class GetTask(
    private val repo: TaskRepo
) {
    fun getTasksByProjectId(projectId: UUID, status: TaskStatus?, assigneeId: UUID?): List<Task> {
        return repo.getAllTasks(projectId, status, assigneeId)
    }
}
