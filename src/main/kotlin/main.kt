import kotlinx.coroutines.runBlocking
import statistics.StatsProcessor
import java.io.File
import java.io.PrintWriter
import kotlin.system.measureTimeMillis

val inputFileName = "tarpit.log"
val resultFileName = "result2.txt"


private fun main() = runBlocking<Unit> {
    val time = measureTimeMillis {
        val inputLog = File(System.getProperty("user.dir") + File.separator + inputFileName)

        val events = inputLog.bufferedReader()
            .lines()
            .parallel()
            .filter { it.endsWith("connected") }  // or "disconnected"
            .map { Event.fromLog(it) }

        val statsProcessor = StatsProcessor(events)
        val output = statsProcessor.evaluate()

        val outFile = File(System.getProperty("user.dir") + File.separator + resultFileName)
        val writer = PrintWriter(outFile)
        writer.write(output)
        writer.close()
        println(output)
    }

    println("Completed in $time ms")
}


