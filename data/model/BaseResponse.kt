package org.zapomni.venturers.data.model

class BaseResponse<T> {
    var status: String? = null
    var data: T? = null
    var message: String? = null

    val isSuccessful: Boolean
        get() = status == "ok"

}