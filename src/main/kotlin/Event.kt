import java.time.LocalDateTime

data class Event(
    val timestamp: LocalDateTime,
    val ip: String,
    val port: Int,
    val type: EventType, // "connected" or "disconnected"
) {

    companion object {

        private const val sampleDateLength = "2021-01-01".length
        private const val sampleTimeLength = "00:00:00".length
        private const val sampleConnectedLength = "connected".length
        private const val sampleUntilIpLength = "2021-05-27 00:47:41 INFO     TarpitServer: Client ('".length
        private const val minIpLength = "0.0.0.0".length

        private const val prefix = "2021-05-27 00:47:41 INFO     TarpitServer: Client ('"

        /**
         * Input: "2021-05-27 00:47:41 INFO     TarpitServer: Client ('154.160.0.156', 39171) connected"
         */
        fun fromLog(line: String): Event {
            val date = line.substring(0, sampleDateLength)
            val time = line.substring(sampleDateLength + 1, sampleDateLength + 1 + sampleTimeLength)

            val indexEndIp = line.indexOf('\'', startIndex = sampleUntilIpLength + minIpLength)
            val ip = line.substring(sampleUntilIpLength, indexEndIp)

            val indexPortStart = indexEndIp + 3 // 3 == ", ".length + 1
            val indexPortEnd = line.indexOf(')', startIndex = indexPortStart)
            val port = line.substring(startIndex = indexPortStart, endIndex = indexPortEnd).toInt()


            val type = if (line[line.length - sampleConnectedLength - 1] == ' ') {
                EventType.CONNECTED
            } else {
                EventType.DISCONNECTED
            }

            return Event(
                timestamp = LocalDateTime.parse("${date}T$time"),
                ip = ip,
                port = port,
                type = type,
            )
        }
    }
}
