package com.core.network.dataproviders

import com.core.network.ApiService
import com.core.network.model.LoginRequest
import com.core.network.model.RegisterRequest
import javax.inject.Inject

class AuthDataProviders @Inject constructor(private val apiService: ApiService) {

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)

    suspend fun register(registerRequest: RegisterRequest) = apiService.register(registerRequest)

}