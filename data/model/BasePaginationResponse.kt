package org.zapomni.venturers.data.model

class BasePaginationResponse<T> {
    val status: String? = null
    val data: T? = null
    val message: String? = null
    val size: Int = 0
    val pageSize: Int = 0
    val page: Int = 0
    val maxPage: Int = 0

    val isSuccessful: Boolean
        get() = status == "ok"

    val lastPage: Boolean
        get() = page == maxPage - 1
}