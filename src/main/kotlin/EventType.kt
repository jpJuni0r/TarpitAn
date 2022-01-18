enum class EventType {
    CONNECTED, DISCONNECTED;

    override fun toString(): String {
        return this.name.lowercase()
    }

    companion object {
        fun fromString(value: String): EventType = when (value) {
            "connected" -> CONNECTED
            "disconnected" -> DISCONNECTED
            else -> throw IllegalArgumentException("Unknown event $value")
        }
    }
}
