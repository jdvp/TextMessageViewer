import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.application
import me.jdvp.tmv.repository.MessageRepository
import me.jdvp.tmv.view.mainWindow
import me.jdvp.tmv.viewmodel.MessageViewModel
import me.jdvp.tmv.viewmodel.PreferenceViewModel
import java.util.prefs.Preferences

fun main() = application {
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
        preferenceViewModel = preferenceViewModel
    )
}