package com.taskflow.auth.data.mappers

import com.taskflow.auth.domain.entities.User
import org.jetbrains.exposed.sql.ResultRow
import com.taskflow.auth.data.tables.UserTable

class AuthQueryMapper {
    fun toUser(row: ResultRow): User = User(
        id = row[UserTable.id],
        name = row[UserTable.name],
        email = row[UserTable.email],
        passwordHash = row[UserTable.passwordHash]
    )
}
