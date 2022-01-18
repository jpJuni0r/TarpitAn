package statistics

import Event

class ConnectedCount : Statistic {
    override fun evaluate(events: List<Event>): String {
        val count = events.count { it.type == EventType.CONNECTED }

        return "Connected: $count"
    }
}