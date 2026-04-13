package com.taskflow.auth.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.uniqueIndex
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object UserTable : Table("users") {
    val id = Table.uuid("id")
    val name = Table.varchar("name", 255)
    val email = Table.varchar("email", 255).uniqueIndex()
    val passwordHash = Table.text("password_hash")
    val createdAt = timestampWithTimeZone("created_at")

    override val primaryKey: PrimaryKey = Table.PrimaryKey(id)
}