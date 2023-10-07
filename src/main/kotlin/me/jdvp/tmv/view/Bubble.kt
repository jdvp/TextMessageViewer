package me.jdvp.tmv.view

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import me.jdvp.tmv.model.EmbeddedBackupFile
import org.jetbrains.skia.Image


@Composable
fun Bubble(
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
fun GenericAttachmentBubble(
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
fun ImageBubble(
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
                bitmap = Image.makeFromEncoded(image.byteArray).toComposeImageBitmap(),
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

@Composable
fun TextBubble(
    body: String,
    subject: String? = null,
    searchText: String? = null,
    messageStyle: MessageStyle
) {
    Bubble(
        bubbleColor = messageStyle.getBubbleColor(),
        contentAlignment = messageStyle.getAlignment()
    ) {
        if (!subject.isNullOrEmpty()) {
            SelectionContainer {
                Text(
                    text = buildAnnotatedString {
                        append("Subject: ")
                    }.plus(buildAnnotatedSearchText(
                        text = subject,
                        searchText = searchText,
                        messageStyle = messageStyle
                    )),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.body2,
                    color = messageStyle.getTextColor(),
                    fontWeight = FontWeight.Bold)
            }
        }
        SelectionContainer {
            Text(
                text = buildAnnotatedSearchText(
                    text = body,
                    searchText = searchText,
                    messageStyle = messageStyle
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.body2,
                color = messageStyle.getTextColor()
            )
        }
    }
}

@Composable
private fun buildAnnotatedSearchText(
    text: String,
    searchText: String? = null,
    messageStyle: MessageStyle
): AnnotatedString {
    return buildAnnotatedString {
        if (searchText.isNullOrEmpty() || !text.contains(searchText, ignoreCase = true)) {
            append(text)
        } else {
            var boldIndices = 0
            text.forEachIndexed { index, c ->
                if (text.startsWith(
                        searchText,
                        ignoreCase = true,
                        startIndex = index
                    )) {
                    boldIndices = searchText.length
                }
                if (boldIndices > 0) {
                    withStyle(
                        SpanStyle(
                        background = messageStyle.getTextColor(),
                        color = messageStyle.getBubbleColor()
                    )
                    ) {
                        append(c)
                    }
                    boldIndices--
                } else {
                    append(c)
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