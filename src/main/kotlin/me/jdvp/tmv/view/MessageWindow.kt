package me.jdvp.tmv.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.model.MessageType

@OptIn(ExperimentalFoundationApi::class)
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
                modifier = Modifier.padding(horizontal = 16.dp)
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

                        when (message.messageType) {
                            MessageType.RECEIVED -> IncomingMessage(message)
                            MessageType.SENT -> OutgoingMessage(message)
                            MessageType.DRAFT -> TODO()
                            MessageType.OUTBOX -> TODO()
                            MessageType.FAILED -> TODO()
                            MessageType.QUEUED -> TODO()
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
@Preview
fun IncomingMessage(message: Message) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        TextBubble(
            text = message.body ?: "",
            bubbleColor = MaterialTheme.colors.secondaryVariant,
            textColor = MaterialTheme.colors.onPrimary
        )
        Spacer(Modifier.fillMaxWidth(fraction = .2F))
    }
}

@Composable
@Preview
fun OutgoingMessage(message: Message) {
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