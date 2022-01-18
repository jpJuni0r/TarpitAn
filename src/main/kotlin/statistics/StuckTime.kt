package statistics

import Event
import EventType
import java.time.Duration
import java.util.stream.Collectors
import java.util.stream.Stream

class StuckTime : Statistic {


    override fun evaluate(events: List<Event>): String {
        val durations = events
            .stream()
            .collect(Collectors.groupingBy { event -> "${event.ip}:${event.port}" })
            .map { entry ->
                val connected = entry.value
                    .filter { it.type == EventType.CONNECTED }
                    .toSet()
                val disconnected = entry.value
                    .filter { it.type == EventType.DISCONNECTED }
                    .toSet()

                var stuck = Duration.ZERO

                val ita = connected.iterator()
                val itb = disconnected.iterator()

                while (ita.hasNext() && itb.hasNext()) {
                    stuck += Duration.between(ita.next().timestamp, itb.next().timestamp)
                }

                stuck
            }

        return listOf(
            totalStuckDuration(durations),
            averageStuckDuration(durations),
            longest(durations),
            shortest(durations),
        ).joinToString(separator = "\n")

    }

    fun totalStuckDuration(connections: List<Duration>): String {
        val result = connections
            .reduce { left, right -> left.plus(right) }

        return "Time in total: ${formatDuration(result)}"
    }

    fun averageStuckDuration(connections: List<Duration>): String {
        val resultSeconds = connections
            .map { it.seconds }
            .average()
        val result = Duration.ofSeconds(resultSeconds.toLong())

        return "Average: ${formatDuration(result)}"
    }

    fun longest(connections: List<Duration>): String {
        val result = connections
            .maxOf { it.seconds }
        val duration = Duration.ofSeconds(result)

        return "Longest in Tarpit: ${formatDuration(duration)}"
    }

    fun shortest(connections: List<Duration>): String {
        val result = connections
            .minOf { it.seconds }
        val duration = Duration.ofSeconds(result)

        return "Shortest in Tarpit: ${formatDuration(duration)}"
    }

    private fun formatDuration(duration: Duration): String {
        return String.format(
            "%02d:%02d:%02d",
            duration.toHoursPart(),
            duration.toMinutesPart(),
            duration.toSecondsPart(),
        )
    }
}
