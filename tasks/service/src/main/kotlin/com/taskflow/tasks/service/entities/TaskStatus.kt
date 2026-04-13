package com.taskflow.tasks.service.entities

import com.fasterxml.jackson.annotation.JsonProperty

enum class TaskStatus {
    @JsonProperty("todo")
    TODO,
    @JsonProperty("in_progress")
    IN_PROGRESS,
    @JsonProperty("done")
    DONE
}
