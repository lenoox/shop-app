package com.lenoox.sendemail.core.monitoring.service

import com.lenoox.sendemail.model.UserRequest

interface LogsService {
    fun logInfo(message: String?, user: UserRequest): Unit
    fun logError(message: String?, user: UserRequest): Unit
}