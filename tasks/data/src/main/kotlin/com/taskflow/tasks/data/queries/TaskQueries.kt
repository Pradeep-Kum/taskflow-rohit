package com.taskflow.tasks.data.queries

import com.taskflow.tasks.data.mappers.TaskQueryMapper
import com.taskflow.tasks.data.tables.TaskProjectTable
import com.taskflow.tasks.data.tables.TaskTable
import com.taskflow.tasks.domain.entities.CreateTaskDraft
import com.taskflow.tasks.domain.entities.Task
import com.taskflow.tasks.domain.entities.TaskStatus
import com.taskflow.tasks.domain.entities.UpdateTaskDraft
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
                    status?.let { s -> andWhere { TaskTable.status eq s.toDbValue() } }
                    assigneeId?.let { a -> andWhere { TaskTable.assigneeId eq a } }
                }
                .map { mapper.toTask(it) }
        }
    }

    fun insertTask(draft: CreateTaskDraft): Task {
        return transaction(db) {
            val newId = UUID.randomUUID()
            val now = OffsetDateTime.now()

            TaskTable.insert {
                it[id] = newId
                it[title] = draft.title
                it[description] = draft.description
                it[status] = draft.status.toDbValue()
                it[priority] = draft.priority.name.lowercase()
                it[projectId] = draft.projectId
                it[assigneeId] = draft.assigneeId
                it[createdById] = draft.createdById
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
                draft.status?.let { s -> it[status] = s.toDbValue() }
                draft.priority?.let { p -> it[priority] = p.name.lowercase() }
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

    fun deleteTask(taskId: UUID, userId: UUID): Boolean? {
        return transaction(db) {
            val access = TaskTable
                .join(
                    otherTable = TaskProjectTable,
                    joinType = JoinType.INNER,
                    onColumn = TaskTable.projectId,
                    otherColumn = TaskProjectTable.id
                )
                .select(TaskTable.id, TaskTable.createdById, TaskProjectTable.ownerId)
                .where { TaskTable.id eq taskId }
                .singleOrNull()
                ?: return@transaction null

            val isProjectOwner = access[TaskProjectTable.ownerId] == userId
            val isTaskCreator = access[TaskTable.createdById] == userId

            if (!isProjectOwner && !isTaskCreator) {
                return@transaction false
            }

            TaskTable.deleteWhere { TaskTable.id eq taskId }
            true
        }
    }
}

private fun TaskStatus.toDbValue(): String {
    return when (this) {
        TaskStatus.TODO -> "todo"
        TaskStatus.IN_PROGRESS -> "in_progress"
        TaskStatus.DONE -> "done"
    }
}
