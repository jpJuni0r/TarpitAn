import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private val lineList = mutableListOf<String>()
private val Liste1 = mutableListOf<String>()
private val Liste2 = mutableListOf<String>()
private val Liste3 = mutableListOf<String>()
private val Liste4 = mutableListOf<String>()
private val Liste5 = mutableListOf<String>()
private var zaehler:Long = 0
private var zaehler2:Long = 0
private var sekundensumme:Long = 0
private var sekundenlongest:Long = 0
private var sekundenshortest:Long = 9999999
private var wertNW: String = ""
private val path = System.getProperty("user.dir")
private var fileName = "ip-locations.txt"
private var fileName2 = "log.txt"
private var fileName3 = "data.txt"
private var fileName4 = "result.txt"
private var message3 = " Reading Data."
private var message5 = " Writing Data."
private var message6 = " Process finished. \r"

private fun main() {
    println(message3)
    val inputStream2: InputStream = File(path + File.separator + fileName).inputStream()
    inputStream2.bufferedReader().forEachLine { Liste5.add(it) }

    val inputStream: InputStream = File(path + File.separator + fileName2).inputStream()

    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    lineList
        .forEach { datenaufbereitung(it) }

    println(message5)
    analyse()
    println("  ")
    println(message6)
}

private fun datenaufbereitung(zeile: String) {

    val pos1 = zeile.indexOf(",", 0)
    val pos2 = zeile.indexOf(",", (pos1 + 1))
    val pos3 = zeile.indexOf(",", (pos2 + 1))

    val wert1: String = zeile.substring(0, pos1)
    val wert2: String = zeile.substring(pos1 + 1, pos2)
    val wert3: String = zeile.substring(pos2 + 1, pos3)
    val wert4: String = zeile.substring(pos3+1)

    Liste1.add(wert1)
    Liste2.add(wert2)
    Liste3.add(wert3)
    Liste4.add(wert4)
}

private fun ipwert(ipdata: String): String {

    val pos1 = ipdata.indexOf(".", 0)
    val pos2 = ipdata.indexOf(".", ((pos1) + 1))
    val pos3 = ipdata.indexOf(".", ((pos2) + 1))

    val wert1: String = ipdata.substring(0, pos1)
    val wert2: String = ipdata.substring(pos1 + 1, pos2)
    val wert3: String = ipdata.substring(pos2 + 1, pos3)
    val wert4: String = ipdata.substring(pos3 + 1)

    wertNW  = wert1 + "." + wert2 + "." + wert3

    val dec1: Long = wert1.toLong() * 16777216
    val dec2: Long = wert2.toLong() * 65536
    val dec3: Long = wert3.toLong() * 256
    val dec4: Long = wert4.toLong() * 1
    val superdec: Long = dec1 + dec2 + dec3 + dec4
    val ruecktext: String = superdec.toString()

    return ruecktext
}

private fun analyse(){

    val anzahlzeilen: Int = Liste1.count() -1

    try {
        val fw = FileWriter(path  + File.separator + fileName3, true)

        for (i in 0..anzahlzeilen) {


            if (Liste4.elementAt(i).toString() == "connected" ) {

                zaehler2 = zaehler2 + 1
                var y: Int = i

                for (y in i..anzahlzeilen){
                    if (Liste4.elementAt(y).toString() == "disconnected"){
                        val WertIP1: String = Liste2.elementAt(i).toString()
                        val WertIP2: String = Liste2.elementAt(y).toString()
                        val WertPort1: String = Liste3.elementAt(i).toString()
                        val WertPort2: String = Liste3.elementAt(y).toString()

                        if(WertIP1 == WertIP2 && WertPort1 == WertPort2) {
                            //Treffer
                            val zeitstempel1: String = Liste1.elementAt(i).toString()
                            val zeitstempel2: String = Liste1.elementAt(y).toString()
                            val zeitstempel11: String = zeitstempel1.replace(" ","T")
                            val zeitstempel22: String = zeitstempel2.replace(" ","T")

                            val localDateTime = LocalDateTime.parse(zeitstempel11)
                            val localDateTime2 = LocalDateTime.parse(zeitstempel22)

                            val sekunden: Long = localDateTime.until(localDateTime2, ChronoUnit.SECONDS)

                            if (sekunden > sekundenlongest) { sekundenlongest = sekunden}
                            if (sekunden < sekundenshortest) { sekundenshortest = sekunden}

                            sekundensumme =  sekundensumme + sekunden

                            val hours:Long =   sekunden / 3600
                            val minutes:Long = (sekunden % 3600) / 60
                            val seconds:Long = sekunden % 60

                            val zeit: String = String.format("%02d", hours) + ":" + String.format("%02d", minutes)  + ":" + String.format("%02d", seconds)

                            zaehler = zaehler + 1

                            val decIP4 = ipwert(Liste2.elementAt(i).toString())

                            val text =  zaehler.toString() + "," + Liste2.elementAt(i).toString() +
                                    "," + decIP4 + "," + wertNW +
                                    "," + Liste1.elementAt(i).toString() + "," + sekunden.toString() +
                                    "," + zeit + System.getProperty("line.separator")

                            fw.write(text)

                            break
                        }
                    }
                }
            }

            var percentage = (i.toDouble() / anzahlzeilen.toDouble()) * 100
            var prozent: Int = percentage.toInt()
            print( "   " + prozent.toString() + "% \r")

        }

        fw.close()
    } catch (e: IOException) {
    }

    val hours2:Long =   sekundensumme / 3600
    val minutes2:Long = (sekundensumme % 3600) / 60
    val seconds2:Long = sekundensumme % 60
    val zeit2: String = String.format("%02d", hours2) + ":" + String.format("%02d", minutes2)  + ":" + String.format("%02d", seconds2)
    val durchschnitt: Long = sekundensumme / zaehler

    val hours3:Long =   durchschnitt / 3600
    val minutes3:Long = (durchschnitt % 3600) / 60
    val seconds3:Long = durchschnitt % 60
    val zeit3: String = String.format("%02d", hours3) + ":" + String.format("%02d", minutes3)  + ":" + String.format("%02d", seconds3)

    val hours4:Long =   sekundenlongest / 3600
    val minutes4:Long = (sekundenlongest % 3600) / 60
    val seconds4:Long = sekundenlongest % 60
    val zeit4: String = String.format("%02d", hours4) + ":" + String.format("%02d", minutes4)  + ":" + String.format("%02d", seconds4)

    String
    val stuckinpit:Long = zaehler2 - zaehler
    val text =  "Connected: " + zaehler2.toString() +  System.getProperty("line.separator") +
            "Disconnected: " + zaehler.toString() +  System.getProperty("line.separator") +
            "Stuck in Tarpit: " + stuckinpit.toString() +  System.getProperty("line.separator") +
            "Time in Seconds: " + sekundensumme.toString()  +  System.getProperty("line.separator") +
            "Time in Total: " + zeit2 +  System.getProperty("line.separator") +
            "Average in Seconds: " + durchschnitt.toString()  +  System.getProperty("line.separator") +
            "Average: " + zeit3 +  System.getProperty("line.separator") +
            "Longest in Tarpit: " + zeit4 + System.getProperty("line.separator") +
            "Shortest in Tarpit: " + sekundenshortest.toString()

    try {
        val fw = FileWriter(path + File.separator + fileName4, true)
        fw.write(text)
        fw.close()
    } catch (e: IOException) {
    }
}


