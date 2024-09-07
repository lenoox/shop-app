package com.lenoox.order

import com.lenoox.order.model.Order
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class LogController(
    @Value("\${kafka.topics.order}") val topic: String,

    @Autowired
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun post(@Validated @RequestBody order: Order): ResponseEntity<Any> {
        return try {
            log.info("Receiving order request")
            log.info("Sending message to Kafka {}", order)
            val message: Message<Order> = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader("X-Custom-Header", "Custom header here")
                .build()
            kafkaTemplate.send(message)
            log.info("order is successful")
            ResponseEntity.ok().build()
        } catch (error: Exception) {
            log.error(error.message)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error to send message")
        }
    }
}