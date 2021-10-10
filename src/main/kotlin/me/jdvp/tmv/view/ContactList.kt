package me.jdvp.tmv.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.jdvp.tmv.model.SimpleContact


@Composable
fun ContactList(contacts: List<SimpleContact>, contactSelectionListener: ContactSelectionListener) {
    var selectedItem by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .width(240.dp)
            .background(color = MaterialTheme.colors.surface)
            .border(width = 1.dp, color = MaterialTheme.colors.primary)
    ) {
        val stateVertical = rememberLazyListState(0)
        LazyColumn(
            state = stateVertical,
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(contacts.size) { index ->
                val item = contacts.getOrNull(index) ?: return@items
                Column(
                    Modifier
                        .background(color = if (index == selectedItem) {
                            MaterialTheme.colors.primary
                        } else {
                            Color.Transparent
                        })
                        .fillMaxWidth()
                        .selectable(
                            selected = index == selectedItem,
                            onClick = {
                                selectedItem = index
                                contactSelectionListener.onContactSelected(item.address)
                            }
                        )
                ) {
                    Spacer(Modifier.height(8.dp))
                    val textColor = if (index == selectedItem) {
                        MaterialTheme.colors.onPrimary
                    } else {
                        MaterialTheme.colors.onBackground
                    }
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = if (item.address == item.name) "" else item.address,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    if (index != contacts.lastIndex) {
                        Divider(
                            color = MaterialTheme.colors.primary
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

fun interface ContactSelectionListener {
    fun onContactSelected(address: String)
}