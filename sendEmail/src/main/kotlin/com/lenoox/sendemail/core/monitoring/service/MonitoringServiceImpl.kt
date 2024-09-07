package com.lenoox.sendemail.core.monitoring.service

import com.lenoox.sendemail.core.monitoring.model.EventMonitoringRequest
import com.lenoox.sendemail.core.monitoring.model.MonitoringRequest
import com.lenoox.sendemail.model.UserRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class MonitoringServiceImpl(private val restTemplate: RestTemplate, private val logsService: LogsService) :
    MonitoringService {
    //@TODO create monitoring in microservices
    @Value("\${monitoring.api.url}")
    private val monitoringApiUrl: String? = null

    @Value("\${monitoring.api.token}")
    private val monitoringApiToken: String? = null

    @Value("\${monitoring.api.sourceType}")
    private val monitoringSourceType: String? = null
    override fun sendAlarm(message: String, user: UserRequest): Unit {
        sendAlarmToSplunk(message, user)
    }

    fun sendAlarmToSplunk(message: String, user: UserRequest): Unit {
        if (monitoringApiUrl == null) {
            throw IllegalArgumentException("Api monitoring url cannot be empty")
        }
        if (monitoringSourceType == null) {
            throw IllegalArgumentException("Source type name cannot be empty")
        }
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Splunk $monitoringApiToken")
        }
        val eventMonitoringData = EventMonitoringRequest(
            lenoox_msg = message,
            lenoox_user_id = user.id,
            lenoox_email = user.email
        )

        val monitoringData = MonitoringRequest(
            sourcetype = monitoringSourceType,
            event = eventMonitoringData
        )
        val request = HttpEntity(monitoringData, headers)
        restTemplate.exchange(monitoringApiUrl, HttpMethod.POST, request, String::class.java)

    }
}