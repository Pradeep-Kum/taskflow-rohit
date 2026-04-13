package com.taskflow.tasks.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object TaskProjectTable : Table("projects") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val ownerId = uuid("owner_id")
    val createdAt = timestampWithTimeZone("created_at")
}
