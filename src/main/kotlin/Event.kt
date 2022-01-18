import java.time.LocalDateTime
import java.util.*

data class Event(
    val timestamp: LocalDateTime,
    val ip: String,
    val port: Int,
    val type: EventType, // "connected" or "disconnected"
) {

    companion object {
        /**
         * Input: "2021-05-27 00:47:41 INFO     TarpitServer: Client ('154.160.0.156', 39171) connected"
         */
        fun fromLog(line: String): Event {
            val scanner = Scanner(line)

            val date = scanner.next()
            val time = scanner.next()

            // Skip loglevel, "TarpitServer:", "Client",
            for (i in 1..3) {
                scanner.next()
            }

            // "('61.177.173.9',"
            val ipPart = scanner.next()
            val ip = ipPart.substring(startIndex = 2, endIndex = ipPart.length - 2)

            // "19762)"
            val portPart = scanner.next()
            val port = portPart.substring(startIndex = 0, endIndex = portPart.length - 1).toInt()

            val type = EventType.fromString(scanner.next()) // "connected" or "disconnected"

            return Event(
                timestamp = LocalDateTime.parse("${date}T$time"),
                ip = ip,
                port = port,
                type = type,
            )
        }
    }
}
