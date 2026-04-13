package com.taskflow.api.controller.project

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

fun ApplicationCall.requireUserId(): UUID {
    val userId = principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("user_id")
        ?.asString()
        ?: throw IllegalArgumentException("Authenticated user ID is required")

    return UUID.fromString(userId)
}
