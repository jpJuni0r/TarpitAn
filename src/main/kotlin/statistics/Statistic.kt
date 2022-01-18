package statistics

import Event
import java.util.stream.Stream

interface Statistic {
    fun evaluate(events: List<Event>): String
}