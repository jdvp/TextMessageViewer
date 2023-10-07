package me.jdvp.tmv.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.viewmodel.MessageViewModel
import me.jdvp.tmv.viewmodel.PreferenceViewModel


@Composable
fun searchWindow(
    messageViewModel: MessageViewModel,
    preferenceViewModel: PreferenceViewModel,
    searchWindowActionListener: SearchWindowActionListener
) {
    val state = rememberWindowState(
        width = 800.dp,
        height = 600.dp,
        position = WindowPosition(Alignment.TopEnd)
    )

    Window(
        onCloseRequest = {
            searchWindowActionListener.onWindowClosed()
        },
        state = state,
        title = "Search Conversations"
    ) {
        val allMessages by remember { MessageViewModel.MOST_RECENT_BACKUP_MESSAGES }
        val colors by remember { preferenceViewModel.colors }

        MaterialTheme(colors = colors) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .border(width = 1.dp, color = MaterialTheme.colors.primary)
                ) {
                    var searchText by remember { mutableStateOf("") }

                    searchField(searchText) { updatedSearchTerm ->
                        searchText = updatedSearchTerm
                    }

                    if (searchText.isNotEmpty()) {
                        searchResults(allMessages, searchText)
                    }
                }
            }
        }
    }
}

@Composable
private fun searchResults(allMessages: List<Message>, searchText: String) {
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

            val messages = allMessages.filter { message ->
                message.containsSearchTerm(searchText)
            }

            items(messages.size) { index ->
                val message = messages.getOrNull(index) ?: return@items
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
                            downloadActionListener = { /*downloadActionListener */ }
                        )
                    }

                    message.additionalFiles.filter {
                        it.byteArray != null
                    }.forEach { attachment ->
                        GenericAttachmentBubble(
                            attachment = attachment,
                            messageStyle = messageStyle,
                            downloadActionListener = { /*downloadActionListener */}
                        )
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

interface SearchWindowActionListener {
    fun onWindowClosed()
}