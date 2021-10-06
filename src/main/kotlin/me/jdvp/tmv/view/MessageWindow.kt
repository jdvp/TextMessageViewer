package me.jdvp.tmv.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.model.MessageType
import java.util.*

@Composable
@Preview
fun MessageWindow(groupedMessages: Map<String, List<Message>>) {
    Column {
//TODO search / filter
//        var searchText by remember { mutableStateOf("") }
//
//        TextField(
//            value = searchText,
//            onValueChange = {
//                searchText = it
//            },
//            label = { Text("Search / Filter", modifier = Modifier.background(Color.Transparent)) },
//            modifier = Modifier.fillMaxWidth()
//        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            val stateVertical = rememberLazyListState(0)
            LazyColumn(
                state = stateVertical,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
            ) {

                groupedMessages.filter {
                    // TODO search / filter
                    // it.value.any { message -> message.body?.contains(searchText, ignoreCase = true) ?: false }
                    true
                }.forEach { (timeGroup, messages) ->
                    item {
                        Spacer(Modifier.height(8.dp))
                    }
                    item {
                        Text(
                            text = timeGroup, modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colors.background),
                            textAlign = TextAlign.Center,
                            fontSize = .75.em
                        )
                    }

                    items(messages.size) { index ->
                        val message = messages.getOrNull(index) ?: return@items

                        val messageStyle = getMessageStyle(message.messageType)

                        val isOutgoingMessage = message.messageType != MessageType.RECEIVED

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = messageStyle.getArrangement()
                        ) {
                            if (isOutgoingMessage) {
                                Spacer(Modifier.fillMaxWidth(fraction = .2F))
                            }
                            Bubble(
                                bubbleColor = messageStyle.getBubbleColor()
                            ) {
                                Text(
                                    text = message.body ?: "",
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
                                    textAlign = TextAlign.Left,
                                    style = MaterialTheme.typography.body2,
                                    color = messageStyle.getTextColor()
                                )

                                if (message.encodedImage != null) {
                                    val imageBytes = Base64.getDecoder().decode(message.encodedImage)
                                    Image(
                                        bitmap = org.jetbrains.skija.Image.makeFromEncoded(imageBytes).asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth(.8F)
                                    )
                                }
                            }

                            if (!isOutgoingMessage) {
                                Spacer(Modifier.fillMaxWidth(fraction = .2F))
                            }
                        }

                    }

                    item {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(stateVertical),
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun getMessageStyle(messageType: MessageType): MessageStyle {
    return when (messageType) {
        MessageType.RECEIVED -> object : MessageStyle {
            @Composable
            override fun getArrangement() = Arrangement.Start
            @Composable
            override fun getBubbleColor() = MaterialTheme.colors.secondaryVariant
            @Composable
            override fun getTextColor() = MaterialTheme.colors.onPrimary
        }
        MessageType.SENT -> object : MessageStyle {}
        MessageType.DRAFT -> TODO()
        MessageType.OUTBOX -> TODO()
        MessageType.FAILED -> TODO()
        MessageType.QUEUED -> TODO()
    }
}

/**
 * Styling for a given message based on message type.
 *
 * Defaults to the regular outgoing message style.
 */
private interface MessageStyle {
    @Composable
    fun getArrangement(): Arrangement.Horizontal = Arrangement.End
    @Composable
    fun getBubbleColor(): Color = MaterialTheme.colors.primary
    @Composable
    fun getTextColor(): Color = MaterialTheme.colors.onPrimary
}

@Composable
@Preview
fun Bubble(bubbleColor: Color, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(16.dp)).background(bubbleColor),
        content = content
    )
}