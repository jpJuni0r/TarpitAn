import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import statistics.StatsProcessor
import statistics.StuckTime

import java.time.LocalDateTime

internal class MainKtTest {

    private fun createEvent(
        time: String = "00:47:41",
        ip: String = "154.160.0.156",
        type: EventType = EventType.CONNECTED
    ) = Event(
        timestamp = LocalDateTime.parse("2021-05-27T$time"),
        ip = ip,
        port = 39171,
        type = type,
    )

    @Test
    fun formatLog() {
        val input = "2021-05-27 00:47:41 INFO     TarpitServer: Client ('154.160.0.156', 39171) connected"
        val expected = createEvent()

        val output = Event.fromLog(input)
        output shouldBe expected
    }

    @Test
    fun stuckTime() {
        val events = listOf(
            createEvent(time = "12:00", type = EventType.CONNECTED),
            createEvent(time = "12:30", type = EventType.DISCONNECTED),
        )
        val expected = "Time in seconds: 00:30:00"

        val s = StuckTime()
        val output = s.evaluate(events.stream())
        output shouldBe expected
    }

    @Test
    fun stats() = runTest {
        val events = listOf(
            createEvent(time = "12:00", type = EventType.CONNECTED),
            createEvent(time = "12:30", type = EventType.DISCONNECTED),
            createEvent(time = "14:00", ip = "1.1.1.1", type = EventType.CONNECTED),
            createEvent(time = "15:30", ip = "1.1.1.1", type = EventType.DISCONNECTED),
        )

        val p = StatsProcessor(events.stream())
        val output = p.evaluate()

        println(output)
    }
}