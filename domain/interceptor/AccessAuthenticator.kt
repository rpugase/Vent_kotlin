package org.zapomni.venturers.domain.interceptor

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.zapomni.venturers.data.repository.AuthRepository
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.extensions.log

class AccessAuthenticator(private val preferencesRepository: PreferencesRepository,
                          private val authRepository: AuthRepository) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        val request = response.request().newBuilder()

        val refreshToken = preferencesRepository.refreshToken
        if (refreshToken != null) {
            val accessTokenResponse = authRepository.getAccessTokenSync(refreshToken).execute()

            if (accessTokenResponse.isSuccessful && accessTokenResponse.body()?.isSuccessful == true) {
                val accessToken = accessTokenResponse.body()!!.data?.accessToken
                preferencesRepository.accessToken = accessToken

                log("Access Token: $accessToken")
                request.header("token", accessToken ?: "token")

            } else runRegistrationScreen()
        } else runRegistrationScreen()

        return request.build()
    }

    private fun runRegistrationScreen() {
        preferencesRepository.clearUserData()
        // todo implement broadcast for opening authorization
    }
}