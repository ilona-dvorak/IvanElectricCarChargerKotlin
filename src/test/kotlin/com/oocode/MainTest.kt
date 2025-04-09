package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainTest {
    @Test
    fun `test main function output`() {
        val newOut = ByteArrayOutputStream()
        System.setOut(PrintStream(newOut))
        main()
        System.out.flush()
        assertThat(
            newOut.toString().trim(), equalTo(
                """Best times to plug in:
Mon, 14 Apr 2025 12:00:00 GMT
Mon, 14 Apr 2025 12:30:00 GMT
Mon, 14 Apr 2025 13:00:00 GMT"""
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

    private var server: Http4kServer? = null

    @BeforeEach
    fun startLocalServerPretendingToBeNationalGridEso() {
        val app: HttpHandler = {
            Response(Status.OK).body(
                """"DATE_GMT","TIME_GMT","SETTLEMENT_DATE","SETTLEMENT_PERIOD","EMBEDDED_WIND_FORECAST","EMBEDDED_WIND_CAPACITY","EMBEDDED_SOLAR_FORECAST","EMBEDDED_SOLAR_CAPACITY"
"2025-04-09T00:00:00","09:30","2025-04-09T00:00:00",21,901,6606,6688,18720
"2025-04-09T00:00:00","10:00","2025-04-09T00:00:00",22,913,6606,7692,18720
"2025-04-09T00:00:00","10:30","2025-04-09T00:00:00",23,913,6606,8675,18720
"2025-04-09T00:00:00","11:00","2025-04-09T00:00:00",24,949,6606,9481,18720
"2025-04-09T00:00:00","11:30","2025-04-09T00:00:00",25,949,6606,9964,18720
"2025-04-09T00:00:00","12:00","2025-04-09T00:00:00",26,1003,6606,10181,18720"""
            )
        }
        // creates a server on port 8123
        server = app.asServer(SunHttp(8123))
        server?.start()
    }

    @AfterEach
    fun stopLocalServerPretendingToBeNationalGridEso() {
        server?.close()
    }

    @Test
    fun `test findWindiestElectricty`() {
        val newOut = ByteArrayOutputStream()
        System.setOut(PrintStream(newOut))

        findWindiestElectricty("http://localhost:8123")
        System.out.flush()
        assertThat(
            newOut.toString().trim(), equalTo(
                """Best times to plug in:
Wed, 9 Apr 2025 11:00:00 GMT
Wed, 9 Apr 2025 11:30:00 GMT
Wed, 9 Apr 2025 12:00:00 GMT"""
            )
        )
    }
}