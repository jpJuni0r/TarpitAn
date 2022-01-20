package statistics

import Event

class DisconnectedCount : Statistic {
    override fun evaluate(events: List<Event>): String {
        val count = events.count { it.type == EventType.DISCONNECTED }

        return "Disconnected: $count"
    }
}
