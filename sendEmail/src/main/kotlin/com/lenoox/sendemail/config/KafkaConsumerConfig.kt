package com.lenoox.sendemail.config

import com.lenoox.sendemail.model.OrderRequest
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer


@EnableKafka
@Configuration
class KafkaConsumerConfig {
    @Value("\${kafka.bootstrapAddress}")
    private val servers: String = ""

    @Bean
    fun consumerFactory(): DefaultKafkaConsumerFactory<String, OrderRequest> {
        val deserializer: JsonDeserializer<OrderRequest> = JsonDeserializer(
            OrderRequest::class.java
        )
        deserializer.setRemoveTypeHeaders(false)
        deserializer.addTrustedPackages("*")
        deserializer.setUseTypeMapperForKey(true)

        val config: MutableMap<String, Any> = HashMap()

        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        config[ConsumerConfig.GROUP_ID_CONFIG] = "ppr"
        config[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = deserializer
        config[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "true"
        config[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = 1000
        return DefaultKafkaConsumerFactory(config, StringDeserializer(), deserializer)


    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, OrderRequest> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderRequest>()
        factory.consumerFactory = consumerFactory()
        return factory
    }

}