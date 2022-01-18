package statistics

import Event
import kotlinx.coroutines.*
import java.util.stream.Collectors
import java.util.stream.Stream

class StatsProcessor(
    private val events: Stream<Event>
) {

    suspend fun evaluate(): String = coroutineScope {
        val eventsList = events.collect(Collectors.toList())

        val types = listOf(
            ConnectedCount(),
            DisconnectedCount(),
            StuckTime(),
        )

        val result = types.map {
            async { it.evaluate(eventsList) }
        }.map { deferred -> deferred.await() }

        return@coroutineScope result.joinToString(separator = "\n")
    }
}