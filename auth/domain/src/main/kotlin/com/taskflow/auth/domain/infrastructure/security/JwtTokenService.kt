package com.taskflow.auth.domain.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.taskflow.auth.domain.interfaces.TokenService
import java.util.*

class JwtTokenService(
    private val secret: String,
    private val issuer: String
) : TokenService {
    override fun generate(userId: UUID, email: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withClaim("user_id", userId.toString())
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + 86_400_000))
            .sign(Algorithm.HMAC256(secret))
    }

    override fun verify(token: String): UUID? {
        return try {
            val verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build()
            val jwt = verifier.verify(token)
            UUID.fromString(jwt.getClaim("user_id").asString())
        } catch (e: Exception) {
            null
        }
    }
}
