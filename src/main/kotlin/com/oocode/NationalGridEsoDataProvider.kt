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

private const val nationalGridEsoDataUrl =
    "https://api.nationalgrideso.com/dataset/91c0c70e-0ef5-4116-b6fa-7ad084b5e0e8/resource/db6c038f-98af-4570-ab60-24d71ebd0ae5/download/embedded-forecast.csv"

class NationalGridEsoDataProvider(
    private val httpHandler: HttpHandler = httpHandler(),
    private val url: String = nationalGridEsoDataUrl
) {
    fun data(): List<Row> {
        val contents = httpHandler(Request(Method.GET, url)).bodyString()
        val lines = CSVReader(StringReader(contents)).readAll().toList()
        val rows = lines.drop(1).map { row ->
            Row(
                ZonedDateTime.of(
                    LocalDateTime.parse(row[0])
                        .withHour(row[1].split(":")[0].toInt())
                        .withMinute(row[1].split(":")[1].toInt()),
                    ZoneId.of("GMT")
                ),
                row[4].toInt()
            )
        }
        return rows
    }
}

private fun httpHandler() = OkHttp(OkHttpClient.Builder().followRedirects(true).build())
