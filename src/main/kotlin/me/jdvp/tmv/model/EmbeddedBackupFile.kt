package me.jdvp.tmv.model

import java.util.*

data class EmbeddedBackupFile(
    val originalFileName: String,
    private val data: String
) {
    val byteArray: ByteArray? by lazy {
        return@lazy try {
            Base64.getDecoder().decode(data)
        } catch (ignored: IllegalArgumentException) {
            null
        }
    }
}
