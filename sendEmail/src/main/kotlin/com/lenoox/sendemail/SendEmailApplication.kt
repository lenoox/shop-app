package com.lenoox.sendemail


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@SpringBootApplication
class SendEmailApplication

fun main(args: Array<String>) {

    runApplication<SendEmailApplication>(*args)
}
