package com.taskflow.tasks.domain.usecases

import com.taskflow.tasks.domain.repos.TaskRepo
import java.util.*

class DeleteTask(
    private val taskRepo: TaskRepo
) {

    fun delete(taskId: UUID, userId: UUID) {
        when (taskRepo.deleteTask(taskId, userId)) {
            true -> return
            false -> throw SecurityException("Only the project owner or task creator can delete this task")
            null -> throw NoSuchElementException("Task $taskId not found")
        }
    }
}
