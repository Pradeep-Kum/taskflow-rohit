package com.taskflow.auth.domain.infrastructure.security

import com.taskflow.auth.domain.interfaces.PasswordHasher
import org.mindrot.jbcrypt.BCrypt

class BCryptPasswordHasher : PasswordHasher {
    override fun hash(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(12))
    override fun check(password: String, hash: String): Boolean = BCrypt.checkpw(password, hash)
}
