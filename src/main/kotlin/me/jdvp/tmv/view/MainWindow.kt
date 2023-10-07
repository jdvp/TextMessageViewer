package me.jdvp.tmv.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import me.jdvp.tmv.model.EmbeddedBackupFile
import me.jdvp.tmv.viewmodel.MessageViewModel
import me.jdvp.tmv.viewmodel.PreferenceViewModel
import java.awt.event.KeyEvent


@Composable
fun mainWindow(
    applicationScope: ApplicationScope,
    messageViewModel: MessageViewModel,
    preferenceViewModel: PreferenceViewModel,
    mainWindowActionListener: MainWindowActionListener
) {
    val state = rememberWindowState(
        width = 800.dp,
        height = 600.dp,
        position = WindowPosition(Alignment.TopStart)
    )

    Window(
        onCloseRequest = applicationScope::exitApplication,
        state = state,
        title = "Text Message Viewer"
    ) {
        val viewState by remember { messageViewModel.viewState }
        val colors by remember { preferenceViewModel.colors }
        var isShowingBackupChooser by remember { mutableStateOf(false) }
        var pendingSave by remember { mutableStateOf<EmbeddedBackupFile?>(null) }
        var conversationSearchEnabled by remember { mutableStateOf(false) }

        MaterialTheme(colors = colors) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colors.surface),
                contentAlignment = Alignment.Center
            ) {
                if (viewState.isLoading) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Loading Message Data",
                            color = MaterialTheme.colors.onBackground
                        )
                        Spacer(Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }
                }

                if (viewState.contacts.isNotEmpty()) {
                    Row {
                        ContactList(viewState.contacts) { selectedAddress ->
                            messageViewModel.filterByAddress(selectedAddress)
                        }

                        MessageWindow(viewState.selectedMessages, conversationSearchEnabled) { file ->
                            pendingSave = null
                            pendingSave = file
                        }
                    }
                } else if (!viewState.isLoading) {
                    Button(onClick = {
                        isShowingBackupChooser = true
                    }) {
                        Text("Load text messages")
                    }

                }
            }

            if (isShowingBackupChooser) {
                FileChooserDialog(
                    onCloseRequest = { file ->
                        isShowingBackupChooser = false

                        file ?: return@FileChooserDialog

                        messageViewModel.loadBackupFile(file)
                    }
                )
            }

            pendingSave?.apply {
                FileSaverDialog(
                    embeddedBackupFile = this,
                    onCloseRequest = {
                        pendingSave = null
                    }
                )
            }
        }

        LaunchedEffect(state) {
            snapshotFlow { state.size }
                .map {
                    state.size.copy(
                        width = max(state.size.width, 640.dp),
                        height = max(state.size.height, 480.dp)
                    )
                }.launchIn(this)
        }

        MenuBar {
            Menu("File") {
                Item(
                    text = "Open backup file",
                    onClick = { isShowingBackupChooser = true },
                    shortcut = KeyShortcut(key = Key(KeyEvent.VK_O), meta = true)
                )
                Item(
                    text = "Toggle dark mode",
                    onClick = { preferenceViewModel.toggleTheme() },
                    shortcut = KeyShortcut(key = Key(KeyEvent.VK_D), meta = true)
                )
            }

            if (viewState.selectedMessages.isNotEmpty()) {
                Menu("Find") {
                    Item(
                        text = if (conversationSearchEnabled) {
                            "Close conversation search"
                        } else {
                            "Search in conversation"
                        },
                        onClick = { conversationSearchEnabled = !conversationSearchEnabled },
                        shortcut = KeyShortcut(key = Key(KeyEvent.VK_F), meta = true)
                    )
                    Item(
                        text = "Search in all conversations",
                        onClick = { mainWindowActionListener.onSearchOpened() },
                        shortcut = KeyShortcut(key = Key(KeyEvent.VK_F), meta = true, shift = true)
                    )
                    Item(
                        text = "Filter contacts",
                        onClick = { },
                        shortcut = KeyShortcut(key = Key(KeyEvent.VK_C), meta = true)
                    )
                }
            }
        }
    }
}

interface MainWindowActionListener {
    fun onSearchOpened()
}