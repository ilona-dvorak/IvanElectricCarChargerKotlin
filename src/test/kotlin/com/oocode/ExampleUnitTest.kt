package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class ExampleUnitTest {
    @Test
    fun canAssertSomething() {
        assertThat(2 + 2, equalTo(4))
    }
}