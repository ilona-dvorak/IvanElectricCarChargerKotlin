package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/*
Tests the whole "system" - including its external dependency (a National Grid ESO service).

Advantages:
    Shows whether the whole "system" works (to some extent)

Disadvantages:
    Limited in what you can assert about the output, because it depends on the result of the
        external service which is not under our control.
    Could fail due to a problem with the external service rather than our code
    Slow to run
 */

class ExampleSystemTest {
    @Test
    fun canInterpretNationalGridDataCorrectly() {
        val newOut = ByteArrayOutputStream()
        System.setOut(PrintStream(newOut))
        main()
        System.out.flush() // to be sure
        assertThat(newOut.toString(), containsSubstring("Best times to plug in"))
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