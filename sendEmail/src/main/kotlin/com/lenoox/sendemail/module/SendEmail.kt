package com.lenoox.sendemail.module

import com.lenoox.sendemail.core.monitoring.service.LogsService
import com.lenoox.sendemail.core.monitoring.service.MonitoringService
import com.lenoox.sendemail.model.OrderRequest
import com.lenoox.sendemail.service.EmailSenderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch


@Service
class SendEmail @Autowired constructor(
    private val monitoringService: MonitoringService,
    private val logsService: LogsService,
    private val emailSenderService: EmailSenderService
) {
    private var latch = CountDownLatch(1)

    private var payload: String? = null

    fun getLatch(): CountDownLatch {
        return latch
    }

    fun resetLatch() {
        latch = CountDownLatch(1)
    }

    fun getPayload(): String? {
        return payload
    }

    @KafkaListener(topics = ["\${kafka.topics.order}"], groupId = "ppr")
    fun listenGroupFoo(order: OrderRequest) {
        // @TODO Authorization adding a user id and email from token-based authorization if such a user exists in the database.
        // @TODO Authorization For security reasons, the JWT token cannot contain a user id and role.
        try {
            emailSenderService.sendEmail(
                "Order ${order.id}",
                "The order has been accepted for processing",
                order.user.email
            )
            logsService.logInfo("email has been sent", order.user)
        } catch (error: RuntimeException) {
            logsService.logError(error.message.toString(), order.user)
            monitoringService.sendAlarm("email was not sent", order.user)

        }
    }
}