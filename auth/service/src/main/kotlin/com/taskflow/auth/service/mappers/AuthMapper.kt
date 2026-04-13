package com.taskflow.auth.service.mappers

import com.taskflow.domain.entities.AuthResponse as DomainAuthResponse
import com.taskflow.domain.entities.RegisterRequest as DomainRegisterRequest
import com.taskflow.domain.entities.LoginRequest as DomainLoginRequest

import com.taskflow.service.entities.AuthResponse as ServiceAuthResponse
import com.taskflow.service.entities.RegisterRequest as ServiceRegisterRequest
import com.taskflow.service.entities.UserProfile as ServiceUserProfile
import com.taskflow.service.entities.LoginRequest as ServiceLoginRequest

fun ServiceRegisterRequest.toDomainRegisterRequest(): DomainRegisterRequest {
    return DomainRegisterRequest(
        name = this.name,
        email = this.email,
        password = ""
    )
}

fun DomainAuthResponse.toServiceResponse(): ServiceAuthResponse {
    return ServiceAuthResponse(
        token = token,
        user = ServiceUserProfile(
            id = this.user.id,
            name = this.user.name,
            email = this.user.email
        )
    )
}

fun ServiceLoginRequest.toDomainLoginRequest(): DomainLoginRequest {
    return DomainLoginRequest(
        email = this.email,
        password = this.password
    )
}