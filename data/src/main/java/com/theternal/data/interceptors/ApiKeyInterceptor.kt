package com.theternal.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader(
                "X-RapidAPI-Key",
                "fdb57825demsha9f52e197bdc9e4p12a2f6jsn43e509a5d5a5"
            )
            .addHeader(
                "X-RapidAPI-Host",
                "currency-exchange.p.rapidapi.com"
            )
            .build()

        return chain.proceed(newRequest)
    }
}