package mutation.tool.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private const val PATH = "./src/test/resources/fakeProject/src"
private const val FILENAME1 = "Main.java"
private const val FILENAME2 = "MainTest.java"

internal class InspectorTest {

    @Test
    fun testGetAllJavaFiles() {
        val files = getAllJavaFiles(File(PATH))
        assertFalse(files.isEmpty())
        for (file in files) {
            assertTrue(file.name.equals(FILENAME1) || file.name.equals(FILENAME2))
        }
    }
}