package com.taskflow.auth.service.usecases

import com.taskflow.auth.domain.entities.User
import com.taskflow.auth.domain.interfaces.PasswordHasher
import com.taskflow.auth.domain.interfaces.TokenService
import com.taskflow.auth.domain.repos.AuthRepo
import com.taskflow.auth.domain.usecases.LoginUser
import com.taskflow.auth.domain.usecases.RegisterUser
import com.taskflow.auth.service.entities.LoginRequest
import com.taskflow.auth.service.entities.RegisterRequest
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthServicesTest {
    private val userId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    @Test
    fun `register service maps request and response`() {
        val repo = FakeAuthRepo()
        val service = RegisterService(RegisterUser(repo, FakePasswordHasher(), FakeTokenService()))

        val response = service.register(RegisterRequest("Rohit", "rohit@example.com", "password123"))

        assertEquals("Rohit", repo.createdName)
        assertEquals("rohit@example.com", repo.createdEmail)
        assertEquals("hashed-password123", repo.createdHash)
        assertEquals("token-rohit@example.com", response.token)
        assertEquals("Rohit", response.user.name)
    }

    @Test
    fun `login service maps request and response`() {
        val user = User(userId, "Rohit", "rohit@example.com", "hashed-password123")
        val service = LoginService(LoginUser(FakeAuthRepo(existingUser = user), FakePasswordHasher(), FakeTokenService()))

        val response = service.login(LoginRequest("rohit@example.com", "password123"))

        assertEquals("token-rohit@example.com", response.token)
        assertEquals(userId, response.user.id)
        assertEquals("rohit@example.com", response.user.email)
    }

    @Test
    fun `login service rethrows domain failures`() {
        val service = LoginService(LoginUser(FakeAuthRepo(existingUser = null), FakePasswordHasher(), FakeTokenService()))

        assertFailsWith<NoSuchElementException> {
            service.login(LoginRequest("missing@example.com", "password123"))
        }
    }

    private class FakeAuthRepo(
        private val existingUser: User? = null
    ) : AuthRepo {
        var createdName: String? = null
        var createdEmail: String? = null
        var createdHash: String? = null

        override fun createUser(id: UUID, name: String, email: String, hash: String): User {
            createdName = name
            createdEmail = email
            createdHash = hash
            return User(id, name, email, hash)
        }

        override fun findByEmail(email: String): User? = existingUser?.takeIf { it.email == email }
    }

    private class FakePasswordHasher : PasswordHasher {
        override fun hash(password: String): String = "hashed-$password"
        override fun check(password: String, hash: String): Boolean = hash == "hashed-$password"
    }

    private class FakeTokenService : TokenService {
        override fun generate(userId: UUID, email: String): String = "token-$email"
        override fun verify(token: String): UUID? = null
    }
}
