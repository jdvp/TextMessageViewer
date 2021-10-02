import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.jdvp.tmv.model.DisplayedMessage
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.repository.SmsReader
import me.jdvp.tmv.view.ContactList
import me.jdvp.tmv.view.MessageWindow
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
@Preview
fun app() {
    val lightColors = lightColors(
        primary = Color(0xff1b87e5),
        primaryVariant = Color(0xff005bb2),
        secondary = Color(0xff9ccc65),
        secondaryVariant = Color(0xff7cb342),
        background = Color.White,
        surface = Color(0xffeeeeee)
    )

    val theme by remember { mutableStateOf(lightColors) }

    DesktopMaterialTheme(colors = theme) {
        var showingDialog by remember { mutableStateOf(false) }
        var showingLoader by remember { mutableStateOf(false) }
        var smsMessages by remember { mutableStateOf<List<DisplayedMessage>?>(null) }
        var selectedMessages by remember { mutableStateOf<List<DisplayedMessage>?>(null) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (showingLoader && smsMessages == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Loading Message Data")
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }



            if (smsMessages != null) {
                Row {
                    ContactList(smsMessages ?: listOf()) { selectedAddress ->
                        selectedMessages = smsMessages?.filter {
                            it.message.address == selectedAddress
                        }
                    }

                    MessageWindow(selectedMessages ?: listOf())
                }
            } else {
                Button(onClick = {
                    showingDialog = true
                }) {
                    Text("Load text messages")
                }

            }
        }

        if (showingDialog) {
            FileDialog(
                onCloseRequest = { file ->
                    showingLoader = true
                    showingDialog = false

                    println("1st thread ${Thread.currentThread()}")

                    GlobalScope.launch(Dispatchers.IO) {
                        println("2st thread ${Thread.currentThread()}")
                        smsMessages = SmsReader(file).parse()
                        println("3st thread ${Thread.currentThread()}")
                    }
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
    ){
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


@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(File(directory + file))
                }
            }
        }.apply {
            file = "*.xml"
            setFilenameFilter { _, name -> name.endsWith(".xml") }
        }
    },
    dispose = FileDialog::dispose
)