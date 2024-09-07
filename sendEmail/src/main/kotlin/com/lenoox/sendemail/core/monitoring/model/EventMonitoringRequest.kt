package com.lenoox.sendemail.core.monitoring.model

data class EventMonitoringRequest(
    val lenoox_msg: String,
    val lenoox_user_id: Long,
    val lenoox_email: String
)