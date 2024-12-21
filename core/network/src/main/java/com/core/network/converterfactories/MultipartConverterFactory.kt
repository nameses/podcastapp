package com.core.network.converterfactories

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class MultipartConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<Any, RequestBody> {
        return Converter { value ->
            if (value is MultipartBody) {
                value
            } else {
                throw IllegalArgumentException("Only MultipartBody is supported")
            }
        }
    }
}