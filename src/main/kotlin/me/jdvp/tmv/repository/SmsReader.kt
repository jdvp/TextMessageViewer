package me.jdvp.tmv.repository

import com.google.i18n.phonenumbers.PhoneNumberUtil
import me.jdvp.tmv.model.DisplayedMessage
import me.jdvp.tmv.model.IncomingMessage
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.model.OutgoingMessage
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


class SmsReader(private val file: File?) {
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    fun parse(): List<DisplayedMessage> {
        file ?: return emptyList()

        val factory = DocumentBuilderFactory.newInstance()
        factory.isValidating = false
        factory.isIgnoringElementContentWhitespace = true
        val builder = factory.newDocumentBuilder()
        val doc: Document? = builder.parse(file)


        var child: Node? = doc?.documentElement?.firstChild

        val messages = mutableListOf<DisplayedMessage>()
        while (child != null) {
            if (child is Element && "sms".equals(child.nodeName, ignoreCase = true)) {

                val smsMessage = Message(
                    protocol = child.getAttribute("protocol")?.toIntOrNull() ?: 0,
                    address = child.getAttribute("address").normalizedPhoneNumber(),
                    date = child.getAttribute("date")?.toLongOrNull() ?: 0,
                    type = child.getAttribute("type")?.toIntOrNull() ?: 0,
                    subject = child.getAttribute("subject"),
                    body = child.getAttribute("body"),
                    toa = child.getAttribute("toa")?.toIntOrNull() ?: 0,
                    scToa = child.getAttribute("sc_toa")?.toIntOrNull() ?: 0,
                    contactName = child.getAttribute("contact_name").let { name ->
                        if (name != "(Unknown)") name else ""
                    }
                )

                messages.add(if (smsMessage.type == 1) {
                    IncomingMessage(smsMessage)
                } else {
                    OutgoingMessage(smsMessage)
                })
            }
            child = child.nextSibling
        }

        return messages
    }

    private fun String.normalizedPhoneNumber(): String {
        return try {
            val number = phoneNumberUtil.parse(this, "US")
            phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
        } catch (ignored: Exception) {
            this
        }
    }
}