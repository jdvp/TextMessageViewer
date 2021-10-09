package me.jdvp.tmv.model

data class EmbeddedBackupFile(
    val originalFileName: String,
    val bytes: List<Byte>
)
