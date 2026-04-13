package com.taskflow.auth.service.mappers

import com.taskflow.auth.domain.entities.AuthResponse as DomainAuthResponse
import com.taskflow.auth.domain.entities.LoginRequest as DomainLoginRequest
import com.taskflow.auth.domain.entities.RegisterRequest as DomainRegisterRequest
import com.taskflow.auth.service.entities.AuthResponse as ServiceAuthResponse
import com.taskflow.auth.service.entities.LoginRequest as ServiceLoginRequest
import com.taskflow.auth.service.entities.RegisterRequest as ServiceRegisterRequest
import com.taskflow.auth.service.entities.UserProfile as ServiceUserProfile

fun ServiceRegisterRequest.toDomainRegisterRequest(): DomainRegisterRequest {
    return DomainRegisterRequest(
        name = this.name,
        email = this.email,
        password = this.password
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
