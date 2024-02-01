package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ExampleInterceptingStdOutTest {
    @Test
    fun canInterceptStdOut() {
        val newOut = ByteArrayOutputStream()
        System.setOut(PrintStream(newOut))
        print("Some text printed to stdout by the system under test")
        System.out.flush() // to be sure
        assertThat(newOut.toString(), equalTo("Some text printed to stdout by the system under test"))
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