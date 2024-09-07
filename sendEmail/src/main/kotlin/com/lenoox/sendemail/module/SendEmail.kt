package com.lenoox.sendemail.module

import com.lenoox.sendemail.core.monitoring.service.LogsService
import com.lenoox.sendemail.core.monitoring.service.MonitoringService
import com.lenoox.sendemail.model.Order
import com.lenoox.sendemail.model.UserRequest
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class SendEmail(val monitoringService: MonitoringService, val logsService: LogsService) {

    @KafkaListener(topics = ["\${kafka.topics.order}"], groupId = "ppr")
    fun listenGroupFoo(order: Order) {
        // @TODO Authorization adding a user id and email from token-based authorization if such a user exists in the database.
        // @TODO Authorization For security reasons, the JWT token cannot contain a user id and role.
        val user = UserRequest(
            id = 1,
            email = "john.smith@gmail.com",
        )

        try {
            throw RuntimeException("invalid email")
            // @TODO send email to mail tramp
            logsService.logInfo("email has been sent", user)
        } catch (error: RuntimeException) {
            logsService.logError("email was not sent", user)
            monitoringService.sendAlarm("email was not sent", user)

        }
    }
}