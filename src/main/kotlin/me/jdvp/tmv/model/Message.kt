package me.jdvp.tmv.model

data class Message(
    val address: String,
    val date: Long,
    val userVisibleDate: String,
    val subject: String?,
    val body: String?,
    val messageType: MessageType
)