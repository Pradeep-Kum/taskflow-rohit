package com.taskflow.projects.service.entities

data class ProjectDetails(
    val project: Project,
    val tasks: List<ProjectTask>
)
