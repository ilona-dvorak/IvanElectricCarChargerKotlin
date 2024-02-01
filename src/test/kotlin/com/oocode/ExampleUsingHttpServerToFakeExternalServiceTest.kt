package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.server.Http4kServer
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExampleUsingHttpServerToFakeExternalServiceTest {
    private var server: Http4kServer? = null

    @Test
    fun canReadUrl() {
        val httpClient = JavaHttpClient()
        val contents = httpClient(Request(Method.GET, "http://localhost:8123")).bodyString()

        assertThat(contents, equalTo("some test data"))
    }

    @BeforeEach
    fun startLocalServerPretendingToBeNationalGridEso() {
        val app: HttpHandler = { Response(Status.OK).body("some test data") }
        // creates a server on port 8123
        server = app.asServer(SunHttp(8123))
        server?.start()
    }

    @AfterEach
    fun stopLocalServerPretendingToBeNationalGridEso() {
        server?.close()
    }
}
