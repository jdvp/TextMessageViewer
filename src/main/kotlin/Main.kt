import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import me.jdvp.tmv.model.EmbeddedBackupFile
import me.jdvp.tmv.repository.MessageRepository
import me.jdvp.tmv.view.ContactList
import me.jdvp.tmv.view.FileChooserDialog
import me.jdvp.tmv.view.FileSaverDialog
import me.jdvp.tmv.view.MessageWindow
import me.jdvp.tmv.viewmodel.MessageViewModel
import me.jdvp.tmv.viewmodel.PreferenceViewModel
import java.util.prefs.Preferences

fun main() = application {
    val state = rememberWindowState(
        size = WindowSize(800.dp, 600.dp)
    )

    val messageViewModel = MessageViewModel(
        messageRepository = MessageRepository(),
        coroutineScope = rememberCoroutineScope()
    )
    val preferenceViewModel = PreferenceViewModel(
        preferences = Preferences.userNodeForPackage(this.javaClass),
        isSystemDarkMode = isSystemInDarkTheme()
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "Text Message Viewer"
    ) {
        val viewState by remember { messageViewModel.viewState }
        val colors by remember { preferenceViewModel.colors }
        var isShowingBackupChooser by remember { mutableStateOf(false) }
        var pendingSave by remember { mutableStateOf<EmbeddedBackupFile?>(null) }

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

                        MessageWindow(viewState.selectedMessages) { file ->
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
                Item("Open backup file", onClick = { isShowingBackupChooser = true })
                Item("Toggle dark mode", onClick = { preferenceViewModel.toggleTheme() })
            }
        }
    }
}