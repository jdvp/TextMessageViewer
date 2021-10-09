package me.jdvp.tmv.repository

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import me.jdvp.tmv.model.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


class MessageRepository {
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()
    private val currentYear = ZonedDateTime.now(TIME_ZONE).year

    fun parseBackupData(backupFile: File?): BackupData {
        backupFile ?: return BackupData(listOf(), listOf())

        val builder = DocumentBuilderFactory.newInstance().apply {
            isValidating = false
            isIgnoringElementContentWhitespace = true
        }.newDocumentBuilder()
        val doc: Document? = builder.parse(backupFile)

        val messages = mutableListOf<Message>()
        val contacts = mutableSetOf<SimpleContact>()

        doc?.documentElement?.getChildrenOfType("sms", "mms")?.forEach { child ->
            if ("sms".equals(child.nodeName, ignoreCase = true)) {
                parseSms(child).apply {
                    messages.add(first)
                    contacts.add(second)
                }
            } else if ("mms".equals(child.nodeName, ignoreCase = true)) {
                parseMms(child).apply {
                    messages.add(first)
                    contacts.add(second)
                }
            }
        }

        return BackupData(
            messages = messages.sortedBy { it.date },
            contacts = contacts.toList()
                .distinct().sortedBy(SimpleContact::name)
        )
    }

    private fun parseSms(smsElement : Element): Pair<Message, SimpleContact> {
        val address = smsElement.getAttribute("address").normalizedPhoneNumber()
        val date = smsElement.getAttribute("date")?.toLongOrNull() ?: 0
        val subject: String? = smsElement.getAttribute("subject")
        val body: String? = smsElement.getAttribute("body")
        val messageType = MessageType.fromType(smsElement.getAttribute("type")?.toIntOrNull() ?: 0)
        val name = smsElement.getAttribute("contact_name").let { name ->
            if (name != "(Unknown)") name else null
        }

        val dateInstant = Instant.ofEpochMilli(date).atZone(TIME_ZONE)

        val userVisibleDate = if (dateInstant.year == currentYear) {
            THIS_YEAR_FORMAT.format(dateInstant)
        } else {
            PAST_YEAR_FORMAT.format(dateInstant)
        }

        val message = Message(
            address = address,
            date = date,
            userVisibleDate = userVisibleDate,
            subject = subject,
            body = body,
            messageType = messageType
        )

        val contact = SimpleContact(
            address = address,
            name = name ?: address
        )

        return message to contact
    }

    private fun parseMms(smsElement : Element): Pair<Message, SimpleContact> {
        val address = smsElement.getAttribute("address").normalizedPhoneNumber()
        val date = smsElement.getAttribute("date")?.toLongOrNull() ?: 0
        val subject: String? = smsElement.getAttribute("sub")
        val messageType = MessageType.fromType(smsElement.getAttribute("msg_box")?.toIntOrNull() ?: 0)
        val name: String? = smsElement.getAttribute("contact_name").let { name ->
            if (name != "(Unknown)") name else null
        }
        var body: String? = null
        var image: EmbeddedBackupFile? = null

        val dateInstant = Instant.ofEpochMilli(date).atZone(TIME_ZONE)

        val userVisibleDate = if (dateInstant.year == currentYear) {
            THIS_YEAR_FORMAT.format(dateInstant)
        } else {
            PAST_YEAR_FORMAT.format(dateInstant)
        }

        smsElement.getChildrenOfType("parts").forEach { parts ->
            parts.getChildrenOfType("part").forEach { part ->
                val sequenceNumber = part.getAttribute("seq")?.toIntOrNull() ?: 0
                if (sequenceNumber >= 0) {
                    val contentType: String? = part.getAttribute("ct")

                    if (contentType == "text/plain") {
                        body = part.getAttribute("text")
                    } else if (contentType?.contains("image", ignoreCase = true) == true) {
                        try {
                            val bytes = part.getAttribute("data").emptyToNull()
                            val originalName = part.getAttribute("cl").emptyToNull()
                            if (bytes != null && originalName != null) {
                                image = EmbeddedBackupFile(
                                    originalFileName = originalName,
                                    bytes = Base64.getDecoder().decode(bytes).toList()
                                )
                            }
                        } catch (ignored: Exception) {}
                    }
                }
            }
        }

        val message = Message(
            address = address,
            date = date,
            userVisibleDate = userVisibleDate,
            subject = subject,
            body = body,
            messageType = messageType,
            encodedImage = image
        )

        val contact = SimpleContact(
            address = address,
            name = name ?: address
        )

        return message to contact
    }

    private fun Element.getChildrenOfType(vararg types: String): List<Element> {
        var current = firstChild
        val matchingChildren = mutableListOf<Element>()

        while (current != null) {
            if (current.nodeName in types && current is Element) {
                matchingChildren.add(current)
            }
            current = current.nextSibling
        }

        return matchingChildren
    }

    private fun String.normalizedPhoneNumber(): String {
        return try {
            val number = phoneNumberUtil.parse(this, "US")
            phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
        } catch (ignored: NumberParseException) {
            this
        }
    }

    private fun <T: CharSequence> T?.emptyToNull(): T? {
        if (this.isNullOrEmpty() || this.toString().equals("null", ignoreCase = true)) {
            return null
        }
        return this
    }

    companion object {
        private val TIME_ZONE = ZoneId.systemDefault()

        private val THIS_YEAR_FORMAT = DateTimeFormatter.ofPattern("cccc, LLLL dd \u2022 hh:mm a")
            .withZone(TIME_ZONE)

        private val PAST_YEAR_FORMAT = DateTimeFormatter.ofPattern("cccc, LLLL dd, uuuu \u2022 hh:mm a")
            .withZone(TIME_ZONE)
    }
}