package com.taskflow.auth.data.queries

import com.taskflow.auth.data.mappers.AuthQueryMapper
import com.taskflow.auth.data.tables.UserTable
import com.taskflow.auth.domain.entities.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.*

class AuthQueries(
    private val db: Database,
    private val mapper: AuthQueryMapper,
) {
    fun insertUser(id: UUID, name: String, email: String, hash: String): User? {
        return transaction(db) {
            UserTable.insert {
                it[UserTable.id] = id
                it[UserTable.name] = name
                it[UserTable.email] = email
                it[UserTable.passwordHash] = hash
                it[UserTable.createdAt] = OffsetDateTime.now()
            }

            // 2. Select it back to confirm and return domain object
            UserTable.selectAll()
                .where { UserTable.id eq id }
                .map { mapper.toUser(it) }
                .singleOrNull()
        }
    }

    fun findByEmail(email: String): User? {
        return transaction(db) {
            UserTable.selectAll()
                .where { UserTable.email eq email }
                .map { mapper.toUser(it) }
                .singleOrNull()
        }
    }
}
