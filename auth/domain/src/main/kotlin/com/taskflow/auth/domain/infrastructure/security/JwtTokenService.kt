package com.taskflow.auth.domain.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.taskflow.domain.interfaces.TokenService
import java.util.*

class JwtTokenService(
    private val secret: String,
    private val issuer: String
) : TokenService {
    override fun generate(userId: UUID): String {
        return JWT.create()
            .withIssuer(issuer)
            .withClaim("userId", userId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + 36_000_000)) // 10 hours
            .sign(Algorithm.HMAC256(secret))
    }

    override fun verify(token: String): UUID? {
        return try {
            val verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build()
            val jwt = verifier.verify(token)
            UUID.fromString(jwt.getClaim("userId").asString())
        } catch (e: Exception) {
            null
        }
    }
}