package com.core.network.interceptors

import com.core.common.annotations.Authorized
import com.core.common.services.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

/**
 * Interceptor, providing headers in request
 */
class HeaderInjectorInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(addHeaders(request))
    }

    private fun addHeaders(request: Request): Request {
        return request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
    }
}