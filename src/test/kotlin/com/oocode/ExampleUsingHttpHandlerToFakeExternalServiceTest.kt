package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.*
import org.junit.jupiter.api.Test

class ExampleUsingHttpHandlerToFakeExternalServiceTest {
    @Test
    fun canReadUrl() {
        val httpClient: HttpHandler = { Response(Status.OK).body("some test data") }
        val contents = httpClient(Request(Method.GET, "http://localhost:8123")).bodyString()

        assertThat(contents, equalTo("some test data"))
    }
}