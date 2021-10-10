package me.jdvp.tmv.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import me.jdvp.tmv.model.EmbeddedBackupFile
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.model.MessageType

@Composable
@Preview
fun MessageWindow(groupedMessages: Map<String, List<Message>>, downloadActionListener: DownloadActionListener) {
    Column(
        modifier = Modifier.fillMaxSize()
            .border(width = 1.dp, color = MaterialTheme.colors.primary)
    ) {
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
                modifier = Modifier.padding(horizontal = 16.dp).matchParentSize()
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
                            text = timeGroup, modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onBackground,
                            fontSize = .75.em
                        )
                    }

                    items(messages.size) { index ->
                        val message = messages.getOrNull(index) ?: return@items

                        val messageStyle = getMessageStyle(message.messageType)

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = messageStyle.getAlignment()
                        ) {
                            Bubble(
                                bubbleColor = messageStyle.getBubbleColor(),
                                contentAlignment = messageStyle.getAlignment()
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

                                val image = message.encodedImage
                                if (image != null) {
                                    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                                        var displayDownload by remember { mutableStateOf(false) }
                                        Image(
                                            bitmap = org.jetbrains.skija.Image.makeFromEncoded(image.bytes.toByteArray()).asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier.onLongClick(
                                                onClick = { displayDownload = false },
                                                onLongClick = { displayDownload = !displayDownload }
                                            ).drawWithCache {
                                                onDrawWithContent {
                                                    drawContent()
                                                    if (displayDownload) {
                                                        drawRect(Color(0x80000000))
                                                    }
                                                }
                                            }
                                        )
                                        if (displayDownload) {
                                            Button(onClick = {
                                                downloadActionListener.onDownload(image)
                                                displayDownload = false
                                            }) {
                                                Text("Download")
                                            }
                                        }
                                    }
                                }
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
            override fun getAlignment() = Alignment.TopStart
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

/**
 * Styling for a given message based on message type.
 *
 * Defaults to the regular outgoing message style.
 */
private interface MessageStyle {
    @Composable
    fun getAlignment(): Alignment = Alignment.TopEnd
    @Composable
    fun getBubbleColor(): Color = MaterialTheme.colors.primary
    @Composable
    fun getTextColor(): Color = MaterialTheme.colors.onPrimary
}

@Composable
@Preview
private fun Bubble(
    bubbleColor: Color,
    contentAlignment: Alignment,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(.8F), contentAlignment = contentAlignment) {
        Column(
            modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(16.dp)).background(bubbleColor),
            content = content,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.onLongClick(
    onClick: () -> Unit = {},
    onLongClick: () -> Unit
): Modifier = composed {
    combinedClickable(
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() },
        onLongClick = onLongClick,
        onClick = onClick
    )
}


fun interface DownloadActionListener {
    fun onDownload(embeddedBackupFile: EmbeddedBackupFile)
}