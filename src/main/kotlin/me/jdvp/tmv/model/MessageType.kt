package me.jdvp.tmv.model

enum class MessageType(val type: Int) {
    RECEIVED(1),
    SENT(2),
    DRAFT(3),
    OUTBOX(4),
    FAILED(5),
    QUEUED(6);

    companion object {
        fun fromType(type: Int): MessageType {
            return values().firstOrNull{
                it.type == type
            } ?: SENT
        }
    }
}