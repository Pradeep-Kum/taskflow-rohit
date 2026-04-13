package com.taskflow.auth.domain.infrastructure.security

import com.taskflow.domain.interfaces.PasswordHasher
import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordHasher : PasswordHasher {
    override fun hash(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
    override fun check(password: String, hash: String): Boolean = BCrypt.checkpw(password, hash)
}