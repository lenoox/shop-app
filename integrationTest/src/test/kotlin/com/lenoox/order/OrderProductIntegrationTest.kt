package com.lenoox.order

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetup
import com.lenoox.sendemail.SendEmailApplication
import com.lenoox.sendemail.model.OrderRequest
import com.lenoox.sendemail.model.UserRequest
import jakarta.mail.Message
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName


@SpringBootTest
@TestPropertySource(properties = ["spring.kafka.consumer.auto-offset-reset=latest"])
@Testcontainers
internal class OrderProductIntegrationTest {
    @Autowired
    lateinit var restTemplate: RestTemplate

    @Value("\${mail.from}")
    val mailFrom: String? = null
    val smtpSetup = ServerSetup(3025, "localhost", ServerSetup.PROTOCOL_SMTP)

    @RegisterExtension
    var greenMail: GreenMailExtension = GreenMailExtension(smtpSetup).withConfiguration(
        GreenMailConfiguration.aConfig()
            .withUser("john@local.local", "john@local.local", "123456")
    );


    @BeforeEach
    @Throws(Exception::class)
    fun init() {
        greenMail.purgeEmailFromAllMailboxes()
    }

    @Test
    @DisplayName("shouldSendEmailWhenOrderIsCreated")
    fun shouldSendEmailWhenOrderIsCreated() {

        val uws: SpringApplicationBuilder = SpringApplicationBuilder(OrderApplication::class.java)
            .properties(
                "server.port=8023",
            )
        uws.run()

        val pws: SpringApplicationBuilder = SpringApplicationBuilder(SendEmailApplication::class.java)
            .properties(
                "server.port=8024",
            )
        pws.run()

        val order = OrderRequest(
            1,
            "549853021",
            1000, UserRequest(2, "john", "smith", "john.smith@localhost.lcoal")
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val request = HttpEntity(order, headers)
        restTemplate.exchange("http://localhost:8023/order", HttpMethod.POST, request, String::class.java)

        Thread.sleep(10000)

        val messages = greenMail.receivedMessages

        assertEquals(1, messages.size)
        assertEquals("Order ${order.id}", messages[0].getSubject())
        assertEquals("The order has been accepted for processing", GreenMailUtil.getBody(messages[0]))
        assertEquals(mailFrom, GreenMailUtil.getAddressList(messages[0].getFrom()))
        assertEquals(
            order.user.email,
            GreenMailUtil.getAddressList(messages[0].getRecipients(Message.RecipientType.TO))
        )
    }

    companion object {
        @Container
        val kafka: KafkaContainer = KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
        )

        @DynamicPropertySource
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)
        }
    }
}