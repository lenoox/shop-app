package com.lenoox.sendemail.core.monitoring.service

import com.lenoox.sendemail.model.UserRequest
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class LogsServiceImpl : LogsService {
    private val log = LogManager.getLogger(LogsServiceImpl::class.java)
    override fun logInfo(message: String?, user: UserRequest): Unit {
        val userId = user.id
        val logMessage = "User with id $userId:$message"
        log.info(logMessage)
    }

    override fun logError(message: String?, user: UserRequest): Unit {
        val userId = user.id
        val logMessage = "User with id $userId:$message"
        log.error(logMessage)
    }
}