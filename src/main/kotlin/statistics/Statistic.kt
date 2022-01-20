package statistics

import Event

interface Statistic {
    fun evaluate(events: List<Event>): String
}
