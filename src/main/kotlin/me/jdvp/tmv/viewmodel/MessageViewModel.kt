package me.jdvp.tmv.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.jdvp.tmv.model.BackupData
import me.jdvp.tmv.model.Message
import me.jdvp.tmv.model.SimpleContact
import me.jdvp.tmv.repository.MessageRepository
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class MessageViewModel(
    private val messageRepository: MessageRepository,
    private val coroutineScope: CoroutineScope
) {
    private var backupData = BackupData(listOf(), listOf())

    val viewState = mutableStateOf(
        ViewState(
            contacts = listOf(),
            selectedMessages = mapOf(),
            isLoading = false
        )
    )

    fun loadBackupFile(backupFile: File) = coroutineScope.launch(Dispatchers.IO) {
        backupData = BackupData(listOf(), listOf())
        viewState.value = ViewState(
            contacts = listOf(),
            selectedMessages = mapOf(),
            isLoading = true
        )
        backupData = messageRepository.parseBackupData(backupFile) //todo DI
        MOST_RECENT_BACKUP_MESSAGES.value = backupData.messages
        viewState.value = ViewState(
            contacts = backupData.contacts,
            selectedMessages = mapOf(),
            isLoading = false
        )
    }

    fun filterByAddress(address: String) = coroutineScope.launch(Dispatchers.IO) {
        val messages = backupData.messages.filter {
            it.address == address
        }

        var previousTimeGroup = ""
        var previousTime = 0L

        val groupedMessages = messages.groupBy { message ->
            val messageTime = message.date

            val diffToPrevious = abs(messageTime - previousTime)

            previousTime = messageTime
            if (TimeUnit.MILLISECONDS.toMinutes(diffToPrevious) > 15) {
                previousTimeGroup = message.userVisibleDate
            }

            return@groupBy previousTimeGroup
        }

        viewState.value = viewState.value.copy(
            selectedMessages = groupedMessages
        )
    }

    data class ViewState(
        val contacts: List<SimpleContact>,
        val selectedMessages: Map<String, List<Message>>,
        val isLoading: Boolean
    )

    companion object {
        @JvmStatic
        var MOST_RECENT_BACKUP_MESSAGES = mutableStateOf<List<Message>>(listOf())
    }
}