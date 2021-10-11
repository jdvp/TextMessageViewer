package me.jdvp.tmv.model

data class Message(
    val address: String,
    val date: Long,
    val userVisibleDate: String,
    val subject: String?,
    val body: String?,
    val messageType: MessageType,
    val images: List<EmbeddedBackupFile> = listOf(),
    val additionalFiles: List<EmbeddedBackupFile> = listOf()
) {
    fun containsSearchTerm(term: String): Boolean {
        return (body?.contains(term, ignoreCase = true) ?: false) ||
                (subject?.contains(term, ignoreCase = true) ?: false) ||
                (additionalFiles.any { it.originalFileName.contains(term, ignoreCase = true) })
    }
}