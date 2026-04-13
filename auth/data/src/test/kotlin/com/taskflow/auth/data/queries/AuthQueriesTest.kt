package com.taskflow.auth.data.queries

import com.taskflow.auth.data.mappers.AuthQueryMapper
import com.taskflow.auth.data.repos.AuthPsqlRepo
import com.taskflow.auth.data.tables.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthQueriesTest {
    private lateinit var db: Database
    private lateinit var queries: AuthQueries

    @BeforeTest
    fun setUp() {
        db = Database.connect(
            url = "jdbc:h2:mem:auth-${UUID.randomUUID()};MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )
        transaction(db) {
            SchemaUtils.create(UserTable)
        }
        queries = AuthQueries(db, AuthQueryMapper())
    }

    @Test
    fun `insert user stores and returns domain user`() {
        val id = UUID.fromString("11111111-1111-1111-1111-111111111111")

        val user = queries.insertUser(id, "Rohit", "rohit@example.com", "hash")

        assertEquals(id, user?.id)
        assertEquals("Rohit", user?.name)
        assertEquals("rohit@example.com", user?.email)
        assertEquals("hash", user?.passwordHash)
        assertEquals(user, queries.findByEmail("rohit@example.com"))
    }

    @Test
    fun `find by email returns null when missing`() {
        assertNull(queries.findByEmail("missing@example.com"))
    }

    @Test
    fun `repository delegates to auth queries`() {
        val id = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val repo = AuthPsqlRepo(queries)

        repo.createUser(id, "Asha", "asha@example.com", "hash-asha")

        assertEquals("Asha", repo.findByEmail("asha@example.com")?.name)
    }
}
