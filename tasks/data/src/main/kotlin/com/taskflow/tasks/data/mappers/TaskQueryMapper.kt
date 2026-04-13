package com.taskflow.tasks.data.mappers

import com.taskflow.domain.entities.Task
import com.taskflow.domain.entities.TaskPriority
import com.taskflow.domain.entities.TaskStatus
import com.taskflow.tables.TaskTable
import org.jetbrains.exposed.sql.ResultRow

class TaskQueryMapper {
    fun toTask(row: ResultRow): Task {
        return Task(
            id = row[TaskTable.id],
            title = row[TaskTable.title],
            description = row[TaskTable.description],

            status = TaskStatus.valueOf(row[TaskTable.status].uppercase()),
            priority = TaskPriority.valueOf(row[TaskTable.priority].uppercase()),

            projectId = row[TaskTable.projectId],
            assigneeId = row[TaskTable.assigneeId],

            dueDate = row[TaskTable.dueDate],
            createdAt = row[TaskTable.createdAt],
            updatedAt = row[TaskTable.updatedAt]
        )
    }
}