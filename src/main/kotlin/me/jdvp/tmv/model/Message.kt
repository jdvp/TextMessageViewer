package me.jdvp.tmv.model

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class Message(
    val protocol: Int,
    val address: String,
    val date: Long,
    val type: Int,
    val subject: String?,
    val body: String?,
    val toa: Int,
    val scToa: Int,
    val contactName: String?
)

abstract class DisplayedMessage(
    val message: Message
) {
    @Composable
    @Preview
    abstract fun display()
}

class IncomingMessage(message: Message): DisplayedMessage(message) {
    @Composable
    @Preview
    override fun display() {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            TextBubble(
                text = message.body ?: "",
                bubbleColor = MaterialTheme.colors.secondaryVariant,
                textColor = MaterialTheme.colors.onPrimary
            )
            Spacer(Modifier.fillMaxWidth(fraction = .2F))
        }
    }
}

class OutgoingMessage(message: Message): DisplayedMessage(message) {
    @Composable
    @Preview
    override fun display() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Spacer(Modifier.fillMaxWidth(fraction = .2F))
            TextBubble(
                text = message.body ?: "",
                bubbleColor = MaterialTheme.colors.primary,
                textColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
@Preview
fun TextBubble(text: String, bubbleColor: Color, textColor: Color) {
    Box(modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(16.dp))) {
        Text(
            text = text,
            modifier = Modifier
                .wrapContentSize()
                .background(bubbleColor)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.body2,
            color = textColor
        )
    }

}