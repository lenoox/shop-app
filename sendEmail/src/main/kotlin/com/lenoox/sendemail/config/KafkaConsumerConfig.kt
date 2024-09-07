package com.lenoox.sendemail.config

import com.lenoox.sendemail.model.Order
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.serializer.JsonDeserializer


@EnableKafka
@Configuration
class KafkaConsumerConfig(
    @Value("\${kafka.bootstrapAddress}")
    private val servers: String
) {

    @Bean
    fun consumerFactory(): DefaultKafkaConsumerFactory<String, Order> {
        val deserializer: JsonDeserializer<Order> = JsonDeserializer(
            Order::class.java
        )
        deserializer.setRemoveTypeHeaders(false)
        deserializer.addTrustedPackages("*")
        deserializer.setUseTypeMapperForKey(true)

        val config: MutableMap<String, Any> = HashMap()

        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        config[ConsumerConfig.GROUP_ID_CONFIG] = "ppr"
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        config[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = deserializer

        return DefaultKafkaConsumerFactory(config, StringDeserializer(), deserializer)


    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Order> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Order>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        factory.containerProperties.isSyncCommits = true
        return factory
    }

}