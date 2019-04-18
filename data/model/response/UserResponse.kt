package org.zapomni.venturers.data.model.response

data class UserResponse(val id: String,
                        val phone: String,
                        val mail: String? = null,
                        val name: String? = null,
                        val surname: String? = null,
                        val photo: String? = null,
                        val admin: Boolean = false)