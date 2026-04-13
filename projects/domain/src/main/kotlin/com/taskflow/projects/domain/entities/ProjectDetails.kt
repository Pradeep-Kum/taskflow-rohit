package com.taskflow.projects.domain.entities

data class ProjectDetails(
    val project: Project,
    val tasks: List<ProjectTask>
)
