package com.taskflow.projects.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object ProjectTaskTable : Table("tasks") {
    val id = uuid("id")
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val status = varchar("status", 50)
    val priority = varchar("priority", 50)
    val projectId = uuid("project_id")
    val assigneeId = uuid("assignee_id").nullable()
    val dueDate = timestampWithTimeZone("due_date").nullable()
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
}
