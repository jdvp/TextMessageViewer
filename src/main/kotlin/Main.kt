import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import me.jdvp.tmv.view.ContactList
import me.jdvp.tmv.view.FileChooserDialog
import me.jdvp.tmv.view.MessageWindow
import me.jdvp.tmv.viewmodel.MessageViewModel

@Composable
@Preview
fun app() {
    val messageViewModel = MessageViewModel(rememberCoroutineScope())

    val lightColors = lightColors(
        primary = Color(0xff1b87e5),
        primaryVariant = Color(0xff005bb2),
        secondary = Color(0xff9ccc65),
        secondaryVariant = Color(0xff7cb342),
        background = Color.White,
        surface = Color(0xffeeeeee)
    )

    val theme by remember { mutableStateOf(lightColors) }
    val viewState by remember { messageViewModel.viewState }
    var showingDialog by remember { mutableStateOf(false) }

    DesktopMaterialTheme(colors = theme) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (viewState.isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Loading Message Data")
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }

            if (viewState.contacts.isNotEmpty()) {
                Row {
                    ContactList(viewState.contacts) { selectedAddress ->
                        messageViewModel.filterByAddress(selectedAddress)
                    }

                    MessageWindow(viewState.selectedMessages)
                }
            } else if (!viewState.isLoading) {
                Button(onClick = {
                    showingDialog = true
                }) {
                    Text("Load text messages")
                }

            }
        }

        if (showingDialog) {
            FileChooserDialog(
                onCloseRequest = { file ->
                    showingDialog = false

                    file ?: return@FileChooserDialog

                    messageViewModel.loadBackupFile(file)
                }
            )
        }

    }
}

fun main() = application {
    val state = rememberWindowState(
        size = WindowSize(800.dp, 600.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "Text Message Viewer"
    ) {
        app()

        LaunchedEffect(state) {
            snapshotFlow { state.size }
                .map {
                    state.size.copy(
                        width = max(state.size.width, 640.dp),
                        height = max(state.size.height, 480.dp)
                    )
                }.launchIn(this)
        }
    }
}