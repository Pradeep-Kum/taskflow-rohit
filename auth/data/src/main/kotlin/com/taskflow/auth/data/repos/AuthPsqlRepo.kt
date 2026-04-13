package com.taskflow.auth.data.repos

import com.taskflow.auth.data.queries.AuthQueries
import com.taskflow.auth.domain.entities.User
import com.taskflow.auth.domain.repos.AuthRepo
import java.util.*

class AuthPsqlRepo(
    val queries: AuthQueries
): AuthRepo {
    override fun createUser(id: UUID, name: String, email: String, hash: String): User? {
        return queries.insertUser(id, name, email, hash)
    }

    override fun findByEmail(email: String): User?{
        return queries.findByEmail(email)
    }
}
