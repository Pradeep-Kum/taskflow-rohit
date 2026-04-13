package com.taskflow.projects.data.mappers

import com.taskflow.projects.data.tables.ProjectTable
import com.taskflow.projects.data.tables.ProjectTaskTable
import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.entities.ProjectTask
import org.jetbrains.exposed.sql.ResultRow

class ProjectQueryMapper {
    fun toProject(row: ResultRow): Project {
        return Project(
            id = row[ProjectTable.id],
            name = row[ProjectTable.name],
            description = row[ProjectTable.description],
            ownerId = row[ProjectTable.ownerId],
            createdAt = row[ProjectTable.createdAt]
        )
    }

    fun toProjectTask(row: ResultRow): ProjectTask {
        return ProjectTask(
            id = row[ProjectTaskTable.id],
            title = row[ProjectTaskTable.title],
            description = row[ProjectTaskTable.description],
            status = row[ProjectTaskTable.status],
            priority = row[ProjectTaskTable.priority],
            assigneeId = row[ProjectTaskTable.assigneeId],
            dueDate = row[ProjectTaskTable.dueDate],
            createdAt = row[ProjectTaskTable.createdAt],
            updatedAt = row[ProjectTaskTable.updatedAt]
        )
    }
}
