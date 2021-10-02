package me.jdvp.tmv.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import me.jdvp.tmv.model.DisplayedMessage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun MessageWindow(messages: List<DisplayedMessage>) {
    val groupedMessages = messages.groupBy {
        Instant.ofEpochMilli(it.message.date).truncatedTo(ChronoUnit.DAYS)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 16.dp)
    ) {
        val stateVertical = rememberLazyListState(0)
        LazyColumn(
            state = stateVertical,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedMessages.forEach { (day, messages) ->

                val a = DateTimeFormatter.ofPattern("cccc, LLLL dd * hh:mm a").withZone(ZoneId.systemDefault())

                stickyHeader {
                    Text(text = a.format(day), modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colors.background),
                        textAlign = TextAlign.Center,
                        fontSize = .75.em
                    )
                }

                items(messages.size) { index ->
                    val message = messages.getOrNull(index) ?: return@items

                    message.display()
                }
            }
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(stateVertical),
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
        )
    }
}