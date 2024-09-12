package com.lenoox.order.producer

import com.lenoox.order.model.OrderRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class OrderProducer(
    @Value("\${kafka.topics.order}") val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    fun send(order: OrderRequest) {
        val message: Message<OrderRequest> = MessageBuilder
            .withPayload(order)
            .setHeader(KafkaHeaders.TOPIC, topic)
            .build()
        kafkaTemplate.send(message)
    }

}
