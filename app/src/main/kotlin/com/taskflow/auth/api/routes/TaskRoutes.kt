package com.taskflow.auth.api.routes

import com.taskflow.auth.api.controller.task.CreateTaskHttpService
import com.taskflow.auth.api.controller.task.DeleteTaskHttpService
import com.taskflow.auth.api.controller.task.GetTaskHttpService
import com.taskflow.auth.api.controller.task.UpdateTaskHttpService
import io.ktor.server.routing.*

fun Route.taskRouting(
    getTaskHttpService: GetTaskHttpService,
    createTaskHttpService: CreateTaskHttpService,
    updateTaskHttpService: UpdateTaskHttpService,
    deleteTaskHttpService: DeleteTaskHttpService
) {
    route("/projects/{id}/tasks") {
        get {
            getTaskHttpService.getProjectTasks(call)
        }
        post {
            createTaskHttpService.handleCreateTask(call)
        }
    }

    route("/tasks/{taskId}") {
        patch {
            updateTaskHttpService.handleUpdateTask(call)
        }

        delete {
            deleteTaskHttpService.handleDeleteTask(call)
        }
    }
}
