package com.lenoox.sendemail.core.monitoring.model


data class MonitoringRequest(
    val sourcetype: String,
    val event: EventMonitoringRequest
)