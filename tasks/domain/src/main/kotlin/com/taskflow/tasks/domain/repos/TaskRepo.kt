package com.taskflow.tasks.domain.repos

import com.taskflow.domain.entities.Task
import com.taskflow.domain.entities.CreateTaskDraft
import com.taskflow.domain.entities.TaskStatus
import com.taskflow.domain.entities.UpdateTaskDraft
import java.util.UUID

interface TaskRepo {
    fun getAllTasks(projectId: UUID, status: TaskStatus?, assigneeId: UUID?): List<Task>

    fun createTask(draft: CreateTaskDraft): Task

    fun updateTask(taskId: UUID, draft: UpdateTaskDraft): Task?

    fun deleteTask(taskId: UUID)
}