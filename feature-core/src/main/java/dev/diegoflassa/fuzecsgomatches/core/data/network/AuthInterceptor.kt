package dev.diegoflassa.fuzecsgomatches.core.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        builder.header("Authorization", "Bearer $authToken")

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}
