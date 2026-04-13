package com.taskflow.tasks.service.mappers

import com.taskflow.domain.entities.Task as DomainTask
import com.taskflow.domain.entities.TaskStatus as DomainTaskStatus
import com.taskflow.domain.entities.TaskPriority as DomainTaskPriority
import com.taskflow.domain.entities.CreateTaskRequest as DomainCreateTaskRequest
import com.taskflow.domain.entities.UpdateTaskRequest as DomainUpdateTaskRequest

import com.taskflow.service.entities.Task as ServiceTask
import com.taskflow.service.entities.TaskStatus as ServiceTaskStatus
import com.taskflow.service.entities.TaskPriority as ServiceTaskPriority
import com.taskflow.service.entities.CreateTaskRequest as ServiceCreateTaskRequest
import com.taskflow.service.entities.UpdateTaskRequest as ServiceUpdateTaskRequest

fun DomainTask.toService(): ServiceTask {
    return ServiceTask(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status.toServiceStatus(),
        priority = this.priority.toServicePriority(),
        projectId = this.projectId,
        assigneeId = this.assigneeId,
        dueDate = this.dueDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun ServiceCreateTaskRequest.toDomain(): DomainCreateTaskRequest {
    return DomainCreateTaskRequest(
        title = title,
        description = description,
        priority = priority,
        assigneeId = assigneeId,
        dueDate = dueDate
    )
}

fun ServiceUpdateTaskRequest.toDomain(): DomainUpdateTaskRequest {
    return DomainUpdateTaskRequest(
        title = title,
        description = description,
        status = this.status?.toDomainStatus(),
        priority = this.priority?.toDomainPriority(),
        assigneeId = assigneeId,
        dueDate = dueDate
    )
}

private fun DomainTaskStatus.toServiceStatus(): ServiceTaskStatus = when (this) {
    DomainTaskStatus.TO_DO -> ServiceTaskStatus.TO_DO
    DomainTaskStatus.IN_PROGRESS -> ServiceTaskStatus.IN_PROGRESS
    DomainTaskStatus.DONE -> ServiceTaskStatus.DONE
}

private fun ServiceTaskStatus.toDomainStatus(): DomainTaskStatus = when (this) {
    ServiceTaskStatus.TO_DO -> DomainTaskStatus.TO_DO
    ServiceTaskStatus.IN_PROGRESS -> DomainTaskStatus.IN_PROGRESS
    ServiceTaskStatus.DONE -> DomainTaskStatus.DONE
}

private fun DomainTaskPriority.toServicePriority(): ServiceTaskPriority = when (this) {
    DomainTaskPriority.LOW -> ServiceTaskPriority.LOW
    DomainTaskPriority.MEDIUM -> ServiceTaskPriority.MEDIUM
    DomainTaskPriority.HIGH -> ServiceTaskPriority.HIGH
}

private fun ServiceTaskPriority.toDomainPriority(): DomainTaskPriority = when (this) {
    ServiceTaskPriority.LOW -> DomainTaskPriority.LOW
    ServiceTaskPriority.MEDIUM -> DomainTaskPriority.MEDIUM
    ServiceTaskPriority.HIGH -> DomainTaskPriority.HIGH
}