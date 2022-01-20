enum class EventType {
    CONNECTED, DISCONNECTED;

    override fun toString(): String {
        return this.name.lowercase()
    }

}
