package com.taskflow.auth.api.routes

import com.taskflow.api.controller.task.CreateTaskHttpService
import com.taskflow.api.controller.task.DeleteTaskHttpService
import com.taskflow.api.controller.task.GetTaskHttpService
import com.taskflow.api.controller.task.UpdateTaskHttpService
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.taskRouting() {
    val getTaskHttpService by inject<GetTaskHttpService>()
    val createTaskHttpService by inject<CreateTaskHttpService>()
    val updateTaskHttpService by inject<UpdateTaskHttpService>()
    val deleteTaskHttpService by inject<DeleteTaskHttpService>()

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