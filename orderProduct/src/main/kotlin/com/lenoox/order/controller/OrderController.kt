package com.lenoox.order.controller

import com.lenoox.order.model.OrderRequest
import com.lenoox.order.producer.OrderProducer
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class LogController(
    private val orderProducer: OrderProducer
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun post(@Validated @RequestBody order: OrderRequest): ResponseEntity<Any> {
        return try {
            log.info("Receiving order request")
            log.info("Receiving order with id: {}", order.id)
            orderProducer.send(order)
            log.info("order is successful")
            ResponseEntity.ok().build()
        } catch (error: Exception) {
            log.error(error.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error to send message")
        }
    }
}