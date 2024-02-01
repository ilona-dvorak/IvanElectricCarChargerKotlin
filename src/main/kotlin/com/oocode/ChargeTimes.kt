package com.oocode

import org.http4k.core.HttpHandler
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME

fun main() {
    ChargeTimes(NationalGridEsoDataProvider()).printReport()
}

data class Row(val dateTimeGmt: ZonedDateTime, val embeddedWindForecast: Int)

class ChargeTimes(
    private val nationalGridEsoDataProvider: NationalGridEsoDataProvider = NationalGridEsoDataProvider()
) {
    constructor(url: String) : this(NationalGridEsoDataProvider(url = url))
    constructor(httpHandler: HttpHandler) : this(NationalGridEsoDataProvider(httpHandler))

    fun printReport() {
        println(report())
    }

    fun report(): String {
        return "Best times to plug in:\n" + nationalGridEsoDataProvider.rows().asSequence()
            .sortedByDescending { it.embeddedWindForecast }
            .take(3)
            .sortedBy { it.dateTimeGmt }
            .map { it.dateTimeGmt.format(RFC_1123_DATE_TIME) }
            .joinToString("\n")
    }
}
