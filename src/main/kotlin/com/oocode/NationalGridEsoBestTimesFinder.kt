package com.oocode

class NationalGridEsoBestTimesFinder {
    fun bestTimes(rows: List<Row>) = rows
        .sortedByDescending { it.embeddedWindForecast }
        .take(3)
        .map { it.dateTimeGmt }
        .sorted()
}
