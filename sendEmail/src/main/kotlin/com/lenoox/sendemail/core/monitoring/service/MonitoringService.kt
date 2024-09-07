package com.lenoox.sendemail.core.monitoring.service

import com.lenoox.sendemail.model.UserRequest

interface MonitoringService {

    fun sendAlarm(message: String, user: UserRequest): Unit
}