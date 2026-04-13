package com.taskflow.auth.domain.interfaces

import java.util.UUID

interface PasswordHasher {
    fun hash(password: String): String
    fun check(password: String, hash: String): Boolean
}

interface TokenService {
    fun generate(userId: UUID): String
    fun verify(token: String): UUID?
}