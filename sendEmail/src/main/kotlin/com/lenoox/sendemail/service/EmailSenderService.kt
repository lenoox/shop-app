package com.lenoox.sendemail.service

import lombok.extern.java.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
@Log
class EmailSenderService @Autowired constructor(
    private val emailSender: JavaMailSender,
    @Value("\${mail.from}")
    private val mailFrom: String
) {

    fun sendEmail(
        subject: String,
        text: String,
        targetEmail: String
    ) {
        val message = SimpleMailMessage()
        message.setSubject(subject)
        message.setText(text)
        message.setFrom(mailFrom)
        message.setTo(targetEmail)
        emailSender.send(message)
    }
}