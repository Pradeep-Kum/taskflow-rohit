package com.taskflow.projects.data.queries

import com.taskflow.projects.data.mappers.ProjectQueryMapper
import com.taskflow.projects.data.tables.ProjectTable
import com.taskflow.projects.data.tables.ProjectTaskTable
import com.taskflow.projects.domain.entities.CreateProjectDraft
import com.taskflow.projects.domain.entities.Project
import com.taskflow.projects.domain.entities.ProjectDetails
import com.taskflow.projects.domain.entities.UpdateProjectDraft
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.OffsetDateTime
import java.util.UUID

class ProjectQueries(
    private val db: Database,
    private val mapper: ProjectQueryMapper
) {
    fun findAccessibleProjects(userId: UUID): List<Project> {
        return transaction(db) {
            ProjectTable
                .join(
                    otherTable = ProjectTaskTable,
                    joinType = JoinType.LEFT,
                    onColumn = ProjectTable.id,
                    otherColumn = ProjectTaskTable.projectId
                )
                .select(ProjectTable.columns)
                .where {
                    (ProjectTable.ownerId eq userId) or (ProjectTaskTable.assigneeId eq userId)
                }
                .withDistinct()
                .map { mapper.toProject(it) }
        }
    }

    fun findProjectDetails(projectId: UUID, userId: UUID): ProjectDetails? {
        return transaction(db) {
            val project = ProjectTable
                .join(
                    otherTable = ProjectTaskTable,
                    joinType = JoinType.LEFT,
                    onColumn = ProjectTable.id,
                    otherColumn = ProjectTaskTable.projectId
                )
                .select(ProjectTable.columns)
                .where {
                    (ProjectTable.id eq projectId) and
                        ((ProjectTable.ownerId eq userId) or (ProjectTaskTable.assigneeId eq userId))
                }
                .withDistinct()
                .map { mapper.toProject(it) }
                .singleOrNull()
                ?: return@transaction null

            val tasks = ProjectTaskTable.selectAll()
                .where { ProjectTaskTable.projectId eq projectId }
                .map { mapper.toProjectTask(it) }

            ProjectDetails(project = project, tasks = tasks)
        }
    }

    fun insertProject(draft: CreateProjectDraft): Project {
        return transaction(db) {
            val newId = UUID.randomUUID()
            val now = OffsetDateTime.now()

            ProjectTable.insert {
                it[id] = newId
                it[name] = draft.name
                it[description] = draft.description
                it[ownerId] = draft.ownerId
                it[createdAt] = now
            }

            Project(
                id = newId,
                name = draft.name,
                description = draft.description,
                ownerId = draft.ownerId,
                createdAt = now
            )
        }
    }

    fun updateProject(projectId: UUID, draft: UpdateProjectDraft): Project? {
        return transaction(db) {
            ProjectTable.update({ ProjectTable.id eq projectId }) {
                draft.name?.let { value -> it[name] = value }
                if (draft.description != null || draft.name != null) {
                    it[description] = draft.description
                }
            }

            ProjectTable.selectAll()
                .where { ProjectTable.id eq projectId }
                .map { mapper.toProject(it) }
                .singleOrNull()
        }
    }

    fun deleteProject(projectId: UUID): Boolean {
        return transaction(db) {
            ProjectTaskTable.deleteWhere { ProjectTaskTable.projectId eq projectId }
            ProjectTable.deleteWhere { ProjectTable.id eq projectId } > 0
        }
    }

    fun isOwner(projectId: UUID, userId: UUID): Boolean {
        return transaction(db) {
            ProjectTable.selectAll()
                .where { (ProjectTable.id eq projectId) and (ProjectTable.ownerId eq userId) }
                .empty().not()
        }
    }
}
