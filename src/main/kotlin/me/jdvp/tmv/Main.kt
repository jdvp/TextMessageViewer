import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import me.jdvp.tmv.repository.MessageRepository
import me.jdvp.tmv.view.MainWindowActionListener
import me.jdvp.tmv.view.SearchWindowActionListener
import me.jdvp.tmv.view.mainWindow
import me.jdvp.tmv.view.searchWindow
import me.jdvp.tmv.viewmodel.MessageViewModel
import me.jdvp.tmv.viewmodel.PreferenceViewModel
import java.util.prefs.Preferences

fun main() = application {
    var showSearchWindow by remember { mutableStateOf(false) }

    val messageViewModel = MessageViewModel(
        messageRepository = MessageRepository(),
        coroutineScope = rememberCoroutineScope()
    )
    val preferenceViewModel = PreferenceViewModel(
        preferences = Preferences.userNodeForPackage(this.javaClass),
        isSystemDarkMode = isSystemInDarkTheme()
    )

    mainWindow(
        applicationScope = this,
        messageViewModel = messageViewModel,
        preferenceViewModel = preferenceViewModel,
        mainWindowActionListener = object : MainWindowActionListener {
            override fun onSearchOpened() {
                showSearchWindow = true
            }
        }
    )

    if (showSearchWindow) {
        searchWindow(
            messageViewModel = messageViewModel,
            preferenceViewModel = preferenceViewModel,
            searchWindowActionListener = object : SearchWindowActionListener {
                override fun onWindowClosed() {
                    showSearchWindow = false
                }
            }
        )
    }
}