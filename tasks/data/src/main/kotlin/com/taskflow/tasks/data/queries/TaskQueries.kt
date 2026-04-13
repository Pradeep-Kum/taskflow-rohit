package com.taskflow.tasks.data.queries

import com.taskflow.domain.entities.Task
import com.taskflow.domain.entities.CreateTaskDraft
import com.taskflow.domain.entities.TaskStatus
import com.taskflow.domain.entities.UpdateTaskDraft
import com.taskflow.mappers.TaskQueryMapper
import com.taskflow.tables.TaskTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.*

class TaskQueries(
    private val db: Database,
    private val mapper: TaskQueryMapper,
) {
    fun findTasksByProjectId(projectId: UUID, status: TaskStatus?, assigneeId: UUID?): List<Task> {
        return transaction(db) {
            TaskTable.selectAll()
                .where { TaskTable.projectId eq projectId }
                .apply {
                    status?.let { s -> andWhere { TaskTable.status eq s.name } }
                    assigneeId?.let { a -> andWhere { TaskTable.assigneeId eq a } }
                }
                .map { mapper.toTask(it) }
        }
    }

    fun insertTask(draft: CreateTaskDraft): Task {
        return transaction {
            val newId = UUID.randomUUID()
            val now = OffsetDateTime.now()

            TaskTable.insert {
                it[id] = newId
                it[title] = draft.title
                it[description] = draft.description
                it[status] = draft.status.name
                it[priority] = draft.priority.name
                it[projectId] = draft.projectId
                it[assigneeId] = draft.assigneeId
                it[dueDate] = draft.dueDate
                it[createdAt] = now
                it[updatedAt] = now
            }

            Task(
                id = newId,
                title = draft.title,
                description = draft.description,
                status = draft.status,
                priority = draft.priority,
                projectId = draft.projectId,
                assigneeId = draft.assigneeId,
                dueDate = draft.dueDate,
                createdAt = now,
                updatedAt = now
            )
        }
    }

    fun updateTask(taskId: UUID, draft: UpdateTaskDraft): Task? {
        return transaction(db) {
            TaskTable.update({ TaskTable.id eq taskId }) {
                draft.title?.let { t -> it[title] = t }
                draft.description?.let { d -> it[description] = d }
                draft.status?.let { s -> it[status] = s.name }
                draft.priority?.let { p -> it[priority] = p.name }
                draft.assigneeId?.let { a -> it[assigneeId] = a }
                draft.dueDate?.let { dd -> it[dueDate] = dd }

                it[updatedAt] = OffsetDateTime.now()
            }

            TaskTable.selectAll()
                .where { TaskTable.id eq taskId }
                .map { row -> mapper.toTask(row) }
                .singleOrNull()
        }
    }

    fun deleteTask(taskId: UUID) {
        transaction(db) {
            TaskTable.deleteWhere { TaskTable.id eq taskId }
        }
    }
}