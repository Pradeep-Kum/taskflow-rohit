package com.taskflow.tasks.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object TaskTable : Table("tasks") {
    val id = uuid("id")
    val title = varchar("title", 255)
    val description = text("description").nullable()

    val status = varchar("status", 50).index()
    val priority = varchar("priority", 50)

    val projectId = uuid("project_id").index()
    val assigneeId = uuid("assignee_id").index().nullable()
    val createdById = uuid("created_by_id").index().nullable()

    val dueDate = timestampWithTimeZone("due_date").nullable()

    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(id)
}
