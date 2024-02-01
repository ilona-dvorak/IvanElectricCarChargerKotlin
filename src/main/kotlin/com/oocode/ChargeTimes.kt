package com.oocode

import com.opencsv.CSVReader
import okhttp3.OkHttpClient
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import java.io.StringReader
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME

private const val nationalGridEsoDataUrl =
    "https://api.nationalgrideso.com/dataset/91c0c70e-0ef5-4116-b6fa-7ad084b5e0e8/resource/db6c038f-98af-4570-ab60-24d71ebd0ae5/download/embedded-forecast.csv"

fun main() {
    ChargeTimes(httpHandler(), nationalGridEsoDataUrl).printReport()
}

class ChargeTimes(private val httpHandler: HttpHandler = httpHandler(),
                  private val url: String = nationalGridEsoDataUrl) {
    fun printReport() {
        println(report())
    }

    fun report(): String {
        val contents = httpHandler(Request(Method.GET, url)).bodyString()
        val lines = CSVReader(StringReader(contents)).readAll().toList()
        val report = "Best times to plug in:\n" + lines.drop(1)
            .sortedByDescending { it[4].toInt() }
            .take(3)
            .map { row ->
                ZonedDateTime.of(
                    LocalDateTime.parse(row[0])
                        .withHour(row[1].split(":")[0].toInt())
                        .withMinute(row[1].split(":")[1].toInt()),
                    ZoneId.of("GMT")
                )
            }
            .sorted()
            .map { it.format(RFC_1123_DATE_TIME) }
            .joinToString("\n")
        return report
    }
}

private fun httpHandler() = OkHttp(OkHttpClient.Builder().followRedirects(true).build())
