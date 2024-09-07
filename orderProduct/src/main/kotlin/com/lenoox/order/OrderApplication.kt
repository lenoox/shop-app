package com.lenoox.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class OrderApplication

fun main(args: Array<String>) {
    runApplication<OrderApplication>(*args)
}
