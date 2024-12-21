package com.core.network.interceptors

import com.core.common.annotations.Header
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

/**
 * Interceptor that inserts headers into API requests based on the @Header annotation
 */
class HeaderInjectorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val invocation = chain.request().tag(Invocation::class.java)
            ?: return chain.proceed(chain.request())

        val headers = invocation.method().annotations.filterIsInstance<Header>()
        headers.forEach { annotation ->
            request = addHeader(request, annotation)
        }

        return chain.proceed(request)
    }

    private fun addHeader(request: Request, annotation: Header): Request {
        return request.newBuilder()
            .addHeader(annotation.name, annotation.value)
            .build()
    }
}
