package com.taskflow.tasks.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.index
import org.jetbrains.exposed.sql.Table.nullable
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object TaskTable : Table("tasks") {
    val id = Table.uuid("id")
    val title = Table.varchar("title", 255)
    val description = Table.text("description").nullable()

    val status = Table.varchar("status", 50).index()
    val priority = Table.varchar("priority", 50)

    val projectId = Table.uuid("project_id").index()
    val assigneeId = Table.uuid("assignee_id").index().nullable()

    val dueDate = timestampWithTimeZone("due_date").nullable()

    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = Table.PrimaryKey(id)
}