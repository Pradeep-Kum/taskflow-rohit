package com.taskflow.projects.domain.entities

data class CreateProjectRequest(
    val name: String,
    val description: String?
)
