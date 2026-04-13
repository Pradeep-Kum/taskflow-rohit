package com.taskflow.infrastructure

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init(): Database {
        val url = env("DATABASE_URL", "jdbc:postgresql://localhost:5432/taskflow")
        val user = env("DATABASE_USER", "taskflow")
        val password = env("DATABASE_PASSWORD", "taskflow")

        Flyway.configure()
            .dataSource(url, user, password)
            .locations("classpath:db/migration")
            .load()
            .migrate()

        return Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )
    }

    private fun env(name: String, default: String): String {
        return System.getenv(name)?.takeIf { it.isNotBlank() } ?: default
    }
}
