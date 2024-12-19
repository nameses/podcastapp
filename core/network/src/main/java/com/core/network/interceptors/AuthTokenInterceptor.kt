package com.core.network.interceptors

import com.core.common.annotations.Authorized
import com.core.common.services.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.Invocation
import okhttp3.Response
import okhttp3.internal.http.RealInterceptorChain

/**
 * Authorization interceptor, that inserts stored JWT-token to api endpoints with @Authorized
 */
class AuthTokenInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val invocation = chain.request().tag(Invocation::class.java)
                ?: return chain.proceed(chain.request())
        containedOnInvocation(invocation).forEach { annotation ->
            request = handleAnnotation(annotation, request)
        }
        return chain.proceed(request)
    }

    private fun containedOnInvocation(invocation: Invocation): Set<Annotation> {
        return invocation.method().annotations.toSet()
    }

    private fun handleAnnotation(
        annotation: Annotation,
        request: Request,
    ): Request {
        val isJwtTokenExists = runBlocking { tokenManager.containsJwtToken() }

        return if (annotation is Authorized && isJwtTokenExists) {
            addAuthHeader(request)
        } else {
            request
        }
    }

    private fun addAuthHeader(request: Request): Request {
        return request.newBuilder()
            .addHeader("Authorization", "Bearer " + tokenManager.token.toString())
            .build()
    }
}