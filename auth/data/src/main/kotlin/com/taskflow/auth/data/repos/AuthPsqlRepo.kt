package com.taskflow.auth.data.repos

import com.taskflow.domain.entities.User
import com.taskflow.domain.repos.AuthRepo
import com.taskflow.queries.AuthQueries
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