package org.zapomni.venturers.domain.exception

open class ApiException(message: String? = "Неизвестная ошибка") : Exception(message)