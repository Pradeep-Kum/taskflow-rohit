package com.taskflow.tasks.domain.usecases

import com.taskflow.domain.entities.Task
import com.taskflow.domain.entities.TaskStatus
import com.taskflow.domain.repos.TaskRepo
import java.util.UUID

class GetTask(
    private val repo: TaskRepo
) {
    fun getTasksByProjectId(projectId: UUID, status: TaskStatus, assigneeId: UUID?): List<Task> {
        return repo.getAllTasks(projectId, status, assigneeId)
    }
}