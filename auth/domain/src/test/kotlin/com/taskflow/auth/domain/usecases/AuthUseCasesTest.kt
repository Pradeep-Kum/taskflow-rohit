package com.taskflow.auth.domain.usecases

import com.taskflow.auth.domain.entities.LoginRequest
import com.taskflow.auth.domain.entities.RegisterRequest
import com.taskflow.auth.domain.entities.User
import com.taskflow.auth.domain.interfaces.PasswordHasher
import com.taskflow.auth.domain.interfaces.TokenService
import com.taskflow.auth.domain.repos.AuthRepo
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class AuthUseCasesTest {
    private val userId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    @Test
    fun `register hashes password creates user and returns token`() {
        val repo = FakeAuthRepo()
        val useCase = RegisterUser(repo, FakePasswordHasher(), FakeTokenService())

        val response = useCase.execute(RegisterRequest("Rohit", "rohit@example.com", "password123"))

        assertEquals("hashed-password123", repo.createdHash)
        assertEquals("token-rohit@example.com", response.token)
        assertEquals("Rohit", response.user.name)
        assertEquals("rohit@example.com", response.user.email)
        assertNotNull(repo.createdId)
    }

    @Test
    fun `register fails when repository cannot create user`() {
        val useCase = RegisterUser(FakeAuthRepo(createUserResult = null), FakePasswordHasher(), FakeTokenService())

        assertFailsWith<IllegalStateException> {
            useCase.execute(RegisterRequest("Rohit", "rohit@example.com", "password123"))
        }
    }

    @Test
    fun `login returns token for valid password`() {
        val user = User(userId, "Rohit", "rohit@example.com", "hashed-password123")
        val useCase = LoginUser(FakeAuthRepo(existingUser = user), FakePasswordHasher(), FakeTokenService())

        val response = useCase.execute(LoginRequest("rohit@example.com", "password123"))

        assertEquals("token-rohit@example.com", response.token)
        assertEquals(userId, response.user.id)
        assertEquals("Rohit", response.user.name)
    }

    @Test
    fun `login fails for unknown email`() {
        val useCase = LoginUser(FakeAuthRepo(existingUser = null), FakePasswordHasher(), FakeTokenService())

        assertFailsWith<NoSuchElementException> {
            useCase.execute(LoginRequest("missing@example.com", "password123"))
        }
    }

    @Test
    fun `login fails for invalid password`() {
        val user = User(userId, "Rohit", "rohit@example.com", "hashed-password123")
        val useCase = LoginUser(FakeAuthRepo(existingUser = user), FakePasswordHasher(valid = false), FakeTokenService())

        assertFailsWith<NoSuchElementException> {
            useCase.execute(LoginRequest("rohit@example.com", "wrong"))
        }
    }

    private class FakeAuthRepo(
        private val existingUser: User? = null,
        private val createUserResult: User? = User(UUID.randomUUID(), "Rohit", "rohit@example.com", "hashed-password123")
    ) : AuthRepo {
        var createdId: UUID? = null
        var createdHash: String? = null

        override fun createUser(id: UUID, name: String, email: String, hash: String): User? {
            createdId = id
            createdHash = hash
            return createUserResult?.copy(id = id, name = name, email = email, passwordHash = hash)
        }

        override fun findByEmail(email: String): User? = existingUser?.takeIf { it.email == email }
    }

    private class FakePasswordHasher(private val valid: Boolean = true) : PasswordHasher {
        override fun hash(password: String): String = "hashed-$password"
        override fun check(password: String, hash: String): Boolean = valid && hash == "hashed-$password"
    }

    private class FakeTokenService : TokenService {
        override fun generate(userId: UUID, email: String): String = "token-$email"
        override fun verify(token: String): UUID? = null
    }
}
