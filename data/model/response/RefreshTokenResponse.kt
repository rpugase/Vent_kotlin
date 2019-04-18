package org.zapomni.venturers.data.model.response

data class RefreshTokenResponse(
        val userId: String,
        val refreshToken: String
)