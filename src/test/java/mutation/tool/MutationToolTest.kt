package mutation.tool

import mutation.tool.util.MutationToolConfig
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class MutationToolTest {

    @Test
    fun runWithoutTestMutants() {
        val config = MutationToolConfig(File(""), File(""))
        config.testMutants = false
        assertTrue(MutationTool(config).run())
    }
}