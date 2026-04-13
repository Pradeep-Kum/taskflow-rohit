package com.taskflow.tasks.service.entities

import com.fasterxml.jackson.annotation.JsonProperty

enum class TaskPriority {
    @JsonProperty("low")
    LOW,
    @JsonProperty("medium")
    MEDIUM,
    @JsonProperty("high")
    HIGH,
}
