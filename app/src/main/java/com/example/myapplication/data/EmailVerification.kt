package com.example.myapplication.data

import java.util.Properties
import java.util.Random
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailVerification {
    private var verificationCode = 0

    private fun sendVerificationEmail(toEmail: String, verificationCode: String) {
        val username = "@string/system_email"
        val password = "@string/system_email_password"

        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(username))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
            message.subject = "Email Verification"
            message.setText("Your verification code is: $verificationCode")

            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
            println("Failed to send verification email: ${e.message}")
        }
    }

    fun generateAndSendCode(userEmail : String) {
        val random = Random()
        val min = 100000
        val max = 999999
        val code = random.nextInt(max - min + 1) + min
        sendVerificationEmail(userEmail, code.toString())
        this.verificationCode = code
    }

    fun checkCode(code : String) : Boolean {
        return code == this.verificationCode.toString()
    }
}