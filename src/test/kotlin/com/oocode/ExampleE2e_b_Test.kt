package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/*
A variant of ExampleE2e_a_Test - this uses a standard http4k approach to provide an HttpHandler which avoids needing
    to start a real server

Advantages compared to ExampleE2e_a_Test:
    Will run quicker than ExampleE2e_a_Test, and is less likely to have flakiness

Disadvantages compared to ExampleE2e_a_Test:
    Does not test the real http server part of the implementation - but that is covered by http4k itself so in practice
        there is usually little advantage of ExampleE2e_a_Test over this
 */
class ExampleE2e_b_Test {
    @Test
    fun canInterpretNationalGridDataCorrectly() {
        val newOut = ByteArrayOutputStream()
        System.setOut(PrintStream(newOut))
        val underTest = ChargeTimes({ Response(Status.OK).body("""
"DATE_GMT","TIME_GMT","SETTLEMENT_DATE","SETTLEMENT_PERIOD","EMBEDDED_WIND_FORECAST","EMBEDDED_WIND_CAPACITY","EMBEDDED_SOLAR_FORECAST","EMBEDDED_SOLAR_CAPACITY"
"2023-12-11T00:00:00","11:30","2023-12-11T00:00:00",23,1333,6488,2417,15595
"2023-12-11T00:00:00","12:00","2023-12-11T00:00:00",24,1283,6488,2580,15595
"2023-12-11T00:00:00","12:30","2023-12-11T00:00:00",25,1197,6488,2652,15595
"2023-12-11T00:00:00","13:00","2023-12-11T00:00:00",26,1111,6488,2578,15595
"2023-12-11T00:00:00","13:30","2023-12-11T00:00:00",27,1012,6488,2304,15595
"2023-12-11T00:00:00","14:00","2023-12-11T00:00:00",28,913,6488,1849,15595
"2023-12-11T00:00:00","14:30","2023-12-11T00:00:00",29,860,6488,1271,15595
"2023-12-11T00:00:00","15:00","2023-12-11T00:00:00",30,806,6488,701,15595
""".trim()) })
        underTest.printReport()
        System.out.flush() // to be sure
        assertThat(
            newOut.toString().trim(), equalTo(
                """
Best times to plug in:
Mon, 11 Dec 2023 11:30:00 GMT
Mon, 11 Dec 2023 12:00:00 GMT
Mon, 11 Dec 2023 12:30:00 GMT
""".trim()
            )
        )
    }

    private var oldOut: PrintStream? = null

    @BeforeEach
    fun rememberRealSystemOut() {
        oldOut = System.out
    }

    @AfterEach
    fun restoreSystemOut() {
        System.setOut(oldOut)
    }
}
