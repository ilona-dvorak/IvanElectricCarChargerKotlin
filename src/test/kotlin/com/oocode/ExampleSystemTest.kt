package com.oocode

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

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