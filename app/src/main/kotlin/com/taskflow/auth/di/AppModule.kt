package com.taskflow.auth.di

import com.taskflow.api.controller.project.CreateProjectHttpService
import com.taskflow.api.controller.project.DeleteProjectHttpService
import com.taskflow.api.controller.project.GetProjectHttpService
import com.taskflow.api.controller.project.GetProjectsHttpService
import com.taskflow.api.controller.project.UpdateProjectHttpService
import com.taskflow.auth.api.controller.auth.LoginHttpService
import com.taskflow.auth.api.controller.auth.RegisterHttpService
import com.taskflow.auth.api.controller.task.CreateTaskHttpService
import com.taskflow.auth.api.controller.task.DeleteTaskHttpService
import com.taskflow.auth.api.controller.task.GetTaskHttpService
import com.taskflow.auth.api.controller.task.UpdateTaskHttpService
import com.taskflow.auth.data.mappers.AuthQueryMapper
import com.taskflow.auth.data.queries.AuthQueries
import com.taskflow.auth.data.repos.AuthPsqlRepo
import com.taskflow.auth.domain.infrastructure.security.BCryptPasswordHasher
import com.taskflow.auth.domain.infrastructure.security.JwtTokenService
import com.taskflow.auth.domain.interfaces.PasswordHasher
import com.taskflow.auth.domain.interfaces.TokenService
import com.taskflow.auth.domain.repos.AuthRepo
import com.taskflow.auth.domain.usecases.LoginUser
import com.taskflow.auth.domain.usecases.RegisterUser
import com.taskflow.auth.service.usecases.LoginService
import com.taskflow.auth.service.usecases.RegisterService
import com.taskflow.infrastructure.DatabaseFactory
import com.taskflow.projects.data.mappers.ProjectQueryMapper
import com.taskflow.projects.data.queries.ProjectQueries
import com.taskflow.projects.data.repos.ProjectPsqlRepo
import com.taskflow.projects.domain.repos.ProjectRepo
import com.taskflow.projects.domain.usecases.CreateProject
import com.taskflow.projects.domain.usecases.DeleteProject
import com.taskflow.projects.domain.usecases.GetProject
import com.taskflow.projects.domain.usecases.GetProjects
import com.taskflow.projects.domain.usecases.UpdateProject
import com.taskflow.projects.service.usecases.CreateProjectService
import com.taskflow.projects.service.usecases.DeleteProjectService
import com.taskflow.projects.service.usecases.GetProjectService
import com.taskflow.projects.service.usecases.GetProjectsService
import com.taskflow.projects.service.usecases.UpdateProjectService
import com.taskflow.tasks.data.mappers.TaskQueryMapper
import com.taskflow.tasks.data.queries.TaskQueries
import com.taskflow.tasks.data.repos.TaskPsqlRepo
import com.taskflow.tasks.domain.repos.TaskRepo
import com.taskflow.tasks.domain.usecases.CreateTask
import com.taskflow.tasks.domain.usecases.DeleteTask
import com.taskflow.tasks.domain.usecases.GetTask
import com.taskflow.tasks.domain.usecases.UpdateTask
import com.taskflow.tasks.service.usecases.CreateTaskService
import com.taskflow.tasks.service.usecases.DeleteTaskService
import com.taskflow.tasks.service.usecases.GetTaskService
import com.taskflow.tasks.service.usecases.UpdateTaskService
import org.koin.dsl.module

val appModule = module {
    single(createdAtStart = true) { DatabaseFactory.init() }

    single<PasswordHasher> { BCryptPasswordHasher() }
    single<TokenService> {
        JwtTokenService(
            secret = requiredEnv("JWT_SECRET"),
            issuer = env("JWT_ISSUER", "taskflow")
        )
    }

    single { AuthQueryMapper() }
    single { AuthQueries(get(), get()) }
    single<AuthRepo> { AuthPsqlRepo(get()) }
    single { RegisterUser(get(), get(), get()) }
    single { LoginUser(get(), get(), get()) }
    single { RegisterService(get()) }
    single { LoginService(get()) }
    single { RegisterHttpService(get()) }
    single { LoginHttpService(get()) }

    single { ProjectQueryMapper() }
    single { ProjectQueries(get(), get()) }
    single<ProjectRepo> { ProjectPsqlRepo(get()) }
    single { GetProjects(get()) }
    single { GetProject(get()) }
    single { CreateProject(get()) }
    single { UpdateProject(get()) }
    single { DeleteProject(get()) }
    single { GetProjectsService(get()) }
    single { GetProjectService(get()) }
    single { CreateProjectService(get()) }
    single { UpdateProjectService(get()) }
    single { DeleteProjectService(get()) }
    single { GetProjectsHttpService(get()) }
    single { GetProjectHttpService(get()) }
    single { CreateProjectHttpService(get()) }
    single { UpdateProjectHttpService(get()) }
    single { DeleteProjectHttpService(get()) }

    single { TaskQueryMapper() }
    single { TaskQueries(get(), get()) }
    single<TaskRepo> { TaskPsqlRepo(get()) }
    single { GetTask(get()) }
    single { CreateTask(get()) }
    single { UpdateTask(get()) }
    single { DeleteTask(get()) }
    single { GetTaskService(get()) }
    single { CreateTaskService(get()) }
    single { UpdateTaskService(get()) }
    single { DeleteTaskService(get()) }
    single { GetTaskHttpService(get()) }
    single { CreateTaskHttpService(get()) }
    single { UpdateTaskHttpService(get()) }
    single { DeleteTaskHttpService(get()) }
}

private fun env(name: String, default: String): String {
    return System.getenv(name)?.takeIf { it.isNotBlank() } ?: default
}

private fun requiredEnv(name: String): String {
    return System.getenv(name)?.takeIf { it.isNotBlank() }
        ?: error("$name environment variable is required")
}
