package org.zapomni.venturers.domain.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.zapomni.venturers.data.repository.PreferencesRepository

class RequestInterceptor(private val preferencesRepository: PreferencesRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder()
                .addHeader("token", preferencesRepository.accessToken ?: "token")
                .build())
    }
}