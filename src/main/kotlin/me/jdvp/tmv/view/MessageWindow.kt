package me.jdvp.tmv.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import me.jdvp.tmv.model.EmbeddedBackupFile
import me.jdvp.tmv.model.Message

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
            searchField(searchText) { updatedSearchTerm ->
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
                                    searchText = searchText,
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

fun interface DownloadActionListener {
    fun onDownload(embeddedBackupFile: EmbeddedBackupFile)
}