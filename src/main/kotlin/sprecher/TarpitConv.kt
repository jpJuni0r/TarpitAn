import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime

private val Liste1 = mutableListOf<String>()
private val Liste2 = mutableListOf<String>()
private val Listeconfig = mutableListOf<String>()
private val path = System.getProperty("user.dir")
private var fileName = "tarpit.log"
private var fileName2 = "log.txt"
private var fileNameConfig = path + File.separator + "config.txt"
private var zaehler: Long = 0
private var message3 = " Reading Log-File."
private var message4 = " Conversion."
private var message5 = " Writing Log-File."
private var message6 = " Process finished. \r"
private var datumanfang = ""
private var datumende = ""
private var teilweise = ""

fun main(args: Array<String>) {
    println(" ")
    val fileExists = File(fileNameConfig).exists()
    if (fileExists) {
        val inputStream11: InputStream = File(fileNameConfig).inputStream()
        inputStream11.bufferedReader().forEachLine { Listeconfig.add(it) }
        datumanfang = Listeconfig.elementAt(0).toString()
        datumende = Listeconfig.elementAt(1).toString()
        teilweise = "1"
    }

    println(message3)
    val inputStream: InputStream = File(path + File.separator + fileName).inputStream()
    inputStream.bufferedReader().forEachLine { Liste1.add(it) }

    println(message4)
    Liste1.forEach { datenaufbereitung(it) }

    print(message5)
    println(" ")
    schreiben()
    println(" ")
    print(message6)
}
private fun datenaufbereitung(zeile: String) {

    if (zeile.contains("connected") ) {
        val wert1: String = zeile.replace(" INFO     TarpitServer: Client ('", ",")
        val wert2: String = wert1.replace("', ", ",")
        val wert3: String = wert2.replace(") ", ",")

        if (teilweise == "1") {
            val pos1 = wert3.indexOf(",", 0)
            val wert4: String = wert3.substring(0, pos1)
            val localDateTime = LocalDateTime.parse(wert4.replace(" ","T"))
            val localDateTime2 = LocalDateTime.parse(datumanfang.replace(" ","T"))
            val localDateTime3 = LocalDateTime.parse(datumende.replace(" ","T"))
            if (localDateTime >= localDateTime2 && localDateTime <= localDateTime3 ) {
                Liste2.add(wert3)
                zaehler = zaehler + 1
            }
        } else {
            Liste2.add(wert3)
            zaehler = zaehler + 1
        }
    }
}

private fun schreiben() {

    try {
        val fw = FileWriter(path  + File.separator + fileName2, true)

        var zaehler2: Long  = 0
        Liste2.forEach { (it)
            zaehler2 = zaehler2 + 1
            val text = it  + System.getProperty("line.separator")
            fw.write(text)
            var percentage = (zaehler2.toDouble() / zaehler.toDouble()) * 100
            var prozent: Int = percentage.toInt()
            print( "   " + prozent.toString() + "% \r")
        }
        fw.close()
    } catch (e: IOException) {
    }
}
