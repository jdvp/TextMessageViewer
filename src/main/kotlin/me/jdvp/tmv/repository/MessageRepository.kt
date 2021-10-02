package me.jdvp.tmv.repository

import com.google.i18n.phonenumbers.PhoneNumberUtil
import me.jdvp.tmv.model.BackupData
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.model.MessageType
import me.jdvp.tmv.model.SimpleContact
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import java.time.Instant
import java.time.Year
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalField
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

        var child: Node? = doc?.documentElement?.firstChild

        val messages = mutableListOf<Message>()
        val contacts = mutableSetOf<SimpleContact>()
        while (child != null) {
            if (child is Element && "sms".equals(child.nodeName, ignoreCase = true)) {

                val address = child.getAttribute("address").normalizedPhoneNumber()
                val date = child.getAttribute("date")?.toLongOrNull() ?: 0

                val dateInstant = Instant.ofEpochMilli(date).atZone(TIME_ZONE)

                val userVisibleDate = if (dateInstant.year == currentYear) {
                    THIS_YEAR_FORMAT.format(dateInstant)
                } else {
                    PAST_YEAR_FORMAT.format(dateInstant)
                }

                messages.add(Message(
                    address = address,
                    date = date,
                    userVisibleDate = userVisibleDate,
                    subject = child.getAttribute("subject"),
                    body = child.getAttribute("body"),
                    messageType = MessageType.fromType(child.getAttribute("type")?.toIntOrNull() ?: 0)
                ))

                val name = child.getAttribute("contact_name").let { name ->
                    if (name != "(Unknown)") name else null
                }

                contacts.add(
                    SimpleContact(
                        address = address,
                        name = name ?: address
                    )
                )
            }
            child = child.nextSibling
        }

        return BackupData(
            messages = messages,
            contacts = contacts.toList()
                .distinct().sortedBy(SimpleContact::name)
        )
    }

    private fun String.normalizedPhoneNumber(): String {
        return try {
            val number = phoneNumberUtil.parse(this, "US")
            phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
        } catch (ignored: Exception) {
            this
        }
    }

    companion object {
        private val TIME_ZONE = ZoneId.systemDefault()

        private val THIS_YEAR_FORMAT = DateTimeFormatter.ofPattern("cccc, LLLL dd \u2022 hh:mm a")
            .withZone(TIME_ZONE)

        private val PAST_YEAR_FORMAT = DateTimeFormatter.ofPattern("cccc, LLLL dd, uuuu \u2022 hh:mm a")
            .withZone(TIME_ZONE)
    }
}