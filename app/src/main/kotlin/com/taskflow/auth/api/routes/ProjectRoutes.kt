package com.taskflow.auth.api.routes

import com.taskflow.api.controller.project.CreateProjectHttpService
import com.taskflow.api.controller.project.DeleteProjectHttpService
import com.taskflow.api.controller.project.GetProjectHttpService
import com.taskflow.api.controller.project.GetProjectsHttpService
import com.taskflow.api.controller.project.UpdateProjectHttpService
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.projectRouting(
    getProjectsHttpService: GetProjectsHttpService,
    getProjectHttpService: GetProjectHttpService,
    createProjectHttpService: CreateProjectHttpService,
    updateProjectHttpService: UpdateProjectHttpService,
    deleteProjectHttpService: DeleteProjectHttpService
) {
    route("/projects") {
        get {
            getProjectsHttpService.handleGetProjects(call)
        }

        post {
            createProjectHttpService.handleCreateProject(call)
        }

        route("/{id}") {
            get {
                getProjectHttpService.handleGetProject(call)
            }

            patch {
                updateProjectHttpService.handleUpdateProject(call)
            }

            delete {
                deleteProjectHttpService.handleDeleteProject(call)
            }
        }
    }
}
