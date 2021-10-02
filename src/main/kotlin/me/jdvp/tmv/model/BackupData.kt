package me.jdvp.tmv.model

data class BackupData(
    val contacts: List<SimpleContact>,
    val messages: List<Message>
)