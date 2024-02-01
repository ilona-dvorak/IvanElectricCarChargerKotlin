package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/*
Tests all of the "system" which is under our control - excludes the external dependency.
It does this by running an HTTP server which mimics the real external dependency.

Advantages:
    Shows whether that part of the "system" which is under our control, works correctly
    Can assert about the output because everything is under our control
    Allows for refactoring without having to change this test, because this test is testing the overall
        behaviour of the system rather than anything specific to the implementation (apart from the ability to
        specify the URL, which could have been done differently)

Disadvantages:
    Slightly slower to run than it could be because it is starting up a real HTTP server
    Tests everything together, so if the test fails it could be difficult to work out what is wrong
    If there is part of the code which has a lot of logic, and we want to test a lot of permutations of
        inputs for it, it could be difficult to set up this sort of test (or at very least, could be difficult
        to make it neat without having a lot of distractions from the essence of what we want to test).
    Could be difficult for someone reading the test to understand the intention of the system
    Could fail due to a problem with the HTTP server which we use to mimic the real external dependency, rather
        than our code
    Could fail due to the main method being incorrect - this test bypasses the main method
    Does not test whether the configuration of the real system is correct, i.e. the test tells the code what URL
        to use (which corresponds to the HTTP server which we are running to mimic the real external dependency).
        The configuration in the production code could be incorrect, which would mean that even if this tests passes,
        the real system could be broken.
 */

class ExampleE2e_a_Test {
    @Test
    fun canInterpretNationalGridDataCorrectly() {
        val newOut = ByteArrayOutputStream()
        System.setOut(PrintStream(newOut))
        ChargeTimes(url = "http://localhost:8123").printReport()
        System.out.flush() // to be sure
        assertThat(
            newOut.toString().trim(), equalTo("""
Best times to plug in:
Mon, 11 Dec 2023 11:30:00 GMT
Mon, 11 Dec 2023 12:00:00 GMT
Mon, 11 Dec 2023 12:30:00 GMT
""".trim()))
    }

    private var oldOut: PrintStream? = null
    private var server: Http4kServer? = null

    @BeforeEach
    fun startLocalServerPretendingToBeNationalGridEso() {
        val app: HttpHandler = { Response(OK).body(hardCodedContent) }
        // creates a server on port 8123
        server = app.asServer(SunHttp(8123))
        server?.start()
    }

    @AfterEach
    fun stopLocalServerPretendingToBeNationalGridEso() {
        server?.close()
    }

    @BeforeEach
    fun rememberRealSystemOut() {
        oldOut = System.out
    }

    @AfterEach
    fun restoreSystemOut() {
        System.setOut(oldOut)
    }
}

private val hardCodedContent = """
"DATE_GMT","TIME_GMT","SETTLEMENT_DATE","SETTLEMENT_PERIOD","EMBEDDED_WIND_FORECAST","EMBEDDED_WIND_CAPACITY","EMBEDDED_SOLAR_FORECAST","EMBEDDED_SOLAR_CAPACITY"
"2023-12-11T00:00:00","11:30","2023-12-11T00:00:00",23,1333,6488,2417,15595
"2023-12-11T00:00:00","12:00","2023-12-11T00:00:00",24,1283,6488,2580,15595
"2023-12-11T00:00:00","12:30","2023-12-11T00:00:00",25,1197,6488,2652,15595
"2023-12-11T00:00:00","13:00","2023-12-11T00:00:00",26,1111,6488,2578,15595
"2023-12-11T00:00:00","13:30","2023-12-11T00:00:00",27,1012,6488,2304,15595
"2023-12-11T00:00:00","14:00","2023-12-11T00:00:00",28,913,6488,1849,15595
"2023-12-11T00:00:00","14:30","2023-12-11T00:00:00",29,860,6488,1271,15595
"2023-12-11T00:00:00","15:00","2023-12-11T00:00:00",30,806,6488,701,15595
""".trim()
