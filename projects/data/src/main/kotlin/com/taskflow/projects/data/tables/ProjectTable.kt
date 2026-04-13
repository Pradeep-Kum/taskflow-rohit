package com.taskflow.projects.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object ProjectTable : Table("projects") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val ownerId = uuid("owner_id").index()
    val createdAt = timestampWithTimeZone("created_at")

    override val primaryKey = PrimaryKey(id)
}
