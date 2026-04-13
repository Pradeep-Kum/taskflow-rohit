package com.taskflow.projects.service.mappers

import com.taskflow.projects.domain.entities.CreateProjectRequest as DomainCreateProjectRequest
import com.taskflow.projects.domain.entities.Project as DomainProject
import com.taskflow.projects.domain.entities.ProjectDetails as DomainProjectDetails
import com.taskflow.projects.domain.entities.ProjectTask as DomainProjectTask
import com.taskflow.projects.domain.entities.UpdateProjectRequest as DomainUpdateProjectRequest
import com.taskflow.projects.service.entities.CreateProjectRequest as ServiceCreateProjectRequest
import com.taskflow.projects.service.entities.Project as ServiceProject
import com.taskflow.projects.service.entities.ProjectDetails as ServiceProjectDetails
import com.taskflow.projects.service.entities.ProjectTask as ServiceProjectTask
import com.taskflow.projects.service.entities.UpdateProjectRequest as ServiceUpdateProjectRequest

fun DomainProject.toService(): ServiceProject {
    return ServiceProject(
        id = id,
        name = name,
        description = description,
        ownerId = ownerId,
        createdAt = createdAt
    )
}

fun DomainProjectTask.toService(): ServiceProjectTask {
    return ServiceProjectTask(
        id = id,
        title = title,
        description = description,
        status = status,
        priority = priority,
        assigneeId = assigneeId,
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun DomainProjectDetails.toService(): ServiceProjectDetails {
    return ServiceProjectDetails(
        project = project.toService(),
        tasks = tasks.map { it.toService() }
    )
}

fun ServiceCreateProjectRequest.toDomain(): DomainCreateProjectRequest {
    return DomainCreateProjectRequest(
        name = name,
        description = description
    )
}

fun ServiceUpdateProjectRequest.toDomain(): DomainUpdateProjectRequest {
    return DomainUpdateProjectRequest(
        name = name,
        description = description
    )
}
