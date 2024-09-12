package com.lenoox.sendemail

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetup
import com.lenoox.sendemail.model.OrderRequest
import com.lenoox.sendemail.model.UserRequest
import com.lenoox.sendemail.service.EmailSenderService
import jakarta.mail.Message
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class EmailSenderServiceTest {

    @Autowired
    lateinit var emailSenderService: EmailSenderService

    @Value("\${mail.from}")
    val mailFrom: String? = null

    val smtpSetup = ServerSetup(3025, "localhost", ServerSetup.PROTOCOL_SMTP)

    @RegisterExtension
    var greenMail: GreenMailExtension = GreenMailExtension(smtpSetup).withConfiguration(
        GreenMailConfiguration.aConfig()
            .withUser("john@local.local", "john@local.local", "123456")
    );


    @Test
    @DisplayName("ShouldSendEmail")
    fun shouldSendEmail() {
        val order = OrderRequest(
            1,
            "549853021",
            1000, UserRequest(2, "john", "smith", "john.smith@localhost.lcoal")
        )

        emailSenderService.sendEmail(
            "Order ${order.id}",
            "The order has been accepted for processing",
            order.user.email
        )
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


}