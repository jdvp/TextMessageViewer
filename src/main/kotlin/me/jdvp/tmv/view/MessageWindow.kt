package me.jdvp.tmv.view

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import me.jdvp.tmv.model.EmbeddedBackupFile
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.model.MessageType
import org.jetbrains.skia.Image.Companion.makeFromEncoded

@Composable
fun MessageWindow(
    groupedMessages: Map<String, List<Message>>,
    showSearchFilter : Boolean,
    downloadActionListener: DownloadActionListener
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .border(width = 1.dp, color = MaterialTheme.colors.primary)
    ) {
        var searchText by remember { mutableStateOf("") }

        if (showSearchFilter) {
            SearchField(searchText) { updatedSearchTerm ->
                searchText = updatedSearchTerm
            }
        } else {
            searchText = ""
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            val stateVertical = rememberLazyListState(0)
            LazyColumn(
                state = stateVertical,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.matchParentSize()
            ) {

                groupedMessages.filter {
                     it.value.any { message -> message.containsSearchTerm(searchText) }
                }.forEach { (timeGroup, messages) ->
                    val filteredMessages = messages.filter { it.containsSearchTerm(searchText) }
                    item {
                        Text(
                            text = timeGroup, modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onBackground,
                            fontSize = .75.em
                        )
                    }

                    items(filteredMessages.size) { index ->
                        val message = filteredMessages.getOrNull(index) ?: return@items

                        val messageStyle = getMessageStyle(message.messageType)

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = messageStyle.getHorizontalAlignment()
                        ) {
                            if (!message.body.isNullOrEmpty()) {
                                TextBubble(
                                    body = message.body,
                                    subject = message.subject,
                                    messageStyle = messageStyle
                                )
                            }

                            message.images.filter {
                                it.byteArray != null
                            }.forEach { image ->
                                ImageBubble(
                                    image = image,
                                    messageStyle = messageStyle,
                                    downloadActionListener = downloadActionListener
                                )
                            }

                            message.additionalFiles.filter {
                                it.byteArray != null
                            }.forEach { attachment ->
                                GenericAttachmentBubble(
                                    attachment = attachment,
                                    messageStyle = messageStyle,
                                    downloadActionListener = downloadActionListener
                                )
                            }
                        }
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

/**
 * Styling for a given message based on message type.
 *
 * Defaults to the regular outgoing message style.
 */
private interface MessageStyle {
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

@Composable
private fun TextBubble(
    body: String,
    subject: String? = null,
    messageStyle: MessageStyle
) {
    Bubble(
        bubbleColor = messageStyle.getBubbleColor(),
        contentAlignment = messageStyle.getAlignment()
    ) {
        if (!subject.isNullOrEmpty()) {
            Text(
                text = "Subject: $subject",
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.body2,
                color = messageStyle.getTextColor(),
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = body,
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.body2,
            color = messageStyle.getTextColor()
        )
    }
}

@Composable
private fun GenericAttachmentBubble(
    attachment: EmbeddedBackupFile,
    messageStyle: MessageStyle,
    downloadActionListener: DownloadActionListener
) {
    Bubble(
        bubbleColor = messageStyle.getBubbleColor(),
        contentAlignment = messageStyle.getAlignment()
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
                .onClickable (
                    onClick = { downloadActionListener.onDownload(attachment) }
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painterResource("images/attachment.svg"),
                contentDescription = null,
                colorFilter = ColorFilter.tint(messageStyle.getTextColor()),
                modifier = Modifier.fillMaxHeight()
                    .wrapContentWidth()
                    .padding(end = 8.dp),
                contentScale = ContentScale.FillHeight
            )
            Column(Modifier.wrapContentHeight()) {
                Text(
                    text = attachment.originalFileName,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.body2,
                    color = messageStyle.getTextColor()
                )
                Text(
                    text = "Download",
                    modifier = Modifier
                        .wrapContentSize(),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.body2,
                    color = messageStyle.getTextColor(),
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
private fun SearchField(searchText: String, action: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = action,
        label = { Text("Search / Filter", modifier = Modifier.background(Color.Transparent)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = object: TextFieldColors {
            @Composable
            override fun backgroundColor(enabled: Boolean) =
                mutableStateOf(MaterialTheme.colors.background)

            @Composable
            override fun cursorColor(isError: Boolean) =
                mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun indicatorColor(
                enabled: Boolean,
                isError: Boolean,
                interactionSource: InteractionSource
            ) = mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun labelColor(
                enabled: Boolean,
                error: Boolean,
                interactionSource: InteractionSource
            ) = mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun leadingIconColor(enabled: Boolean, isError: Boolean) =
                mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun placeholderColor(enabled: Boolean) =
                mutableStateOf(MaterialTheme.colors.onBackground)

            @Composable
            override fun textColor(enabled: Boolean) =
                mutableStateOf(MaterialTheme.colors.onBackground)

            @Composable
            override fun trailingIconColor(enabled: Boolean, isError: Boolean) =
                mutableStateOf(MaterialTheme.colors.primary)

        }
    )
}

@Composable
private fun ImageBubble(
    image: EmbeddedBackupFile,
    messageStyle: MessageStyle,
    downloadActionListener: DownloadActionListener
) {
    Bubble(
        bubbleColor = messageStyle.getBubbleColor(),
        contentAlignment = messageStyle.getAlignment()
    ) {
        Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
            var displayDownload by remember { mutableStateOf(false) }
            Image(
                bitmap = makeFromEncoded(image.byteArray).toComposeImageBitmap(),
                contentDescription = null,
                modifier = Modifier.onClickable(
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

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.onClickable(
    onClick: () -> Unit,
    onLongClick: () -> Unit = onClick
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