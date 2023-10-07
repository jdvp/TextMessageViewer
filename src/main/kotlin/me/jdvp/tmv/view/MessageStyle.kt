package me.jdvp.tmv.view

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import me.jdvp.tmv.model.MessageType

/**
 * Styling for a given message based on message type.
 *
 * Defaults to the regular outgoing message style.
 */
interface MessageStyle {
    @Composable
    fun getAlignment(): Alignment = Alignment.TopEnd
    @Composable
    fun getHorizontalAlignment(): Alignment.Horizontal = Alignment.End
    @Composable
    fun getBubbleColor(): Color = MaterialTheme.colors.primary
    @Composable
    fun getTextColor(): Color = MaterialTheme.colors.onPrimary
}

@Composable
fun getMessageStyle(messageType: MessageType): MessageStyle {
    return when (messageType) {
        MessageType.RECEIVED -> object : MessageStyle {
            @Composable
            override fun getAlignment() = Alignment.TopStart
            @Composable
            override fun getHorizontalAlignment(): Alignment.Horizontal = Alignment.Start
            @Composable
            override fun getBubbleColor() = MaterialTheme.colors.secondaryVariant
            @Composable
            override fun getTextColor() = MaterialTheme.colors.onPrimary
        }
        MessageType.SENT -> object : MessageStyle {}
        MessageType.DRAFT -> TODO()
        MessageType.OUTBOX -> TODO()
        MessageType.FAILED -> object : MessageStyle {
            @Composable
            override fun getBubbleColor(): Color = MaterialTheme.colors.error
        }
        MessageType.QUEUED -> TODO()
    }
}