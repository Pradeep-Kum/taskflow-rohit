package com.taskflow.auth.domain.repos

import com.taskflow.auth.domain.entities.User
import java.util.*

interface AuthRepo {
    fun createUser(id: UUID, name: String, email: String, hash: String): User?

    fun findByEmail(email: String): User?
}
