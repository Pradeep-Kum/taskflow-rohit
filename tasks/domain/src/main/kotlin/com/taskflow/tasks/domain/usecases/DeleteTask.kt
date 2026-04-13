package com.taskflow.tasks.domain.usecases

import com.taskflow.domain.repos.TaskRepo
import java.util.*

class DeleteTask(
    private val taskRepo: TaskRepo
) {

    fun delete(taskId: UUID) {
        return taskRepo.deleteTask(taskId)
    }
}