package mutation.tool.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private val sourceFolder1 = File("./src/test/resources/fakeProject/src/main/java")
private val sourceFolder2 = File("./src/test/resources/fakeProject/src/main")
private val testFolder1 = File("./src/test/resources/fakeProject/src/test/java")
private val testFolder2 = File("./src/test/resources/fakeProject/src/test")

internal class FolderOperationsTest {

    @Test
    fun notContainsSubpath() {
        assertFalse(isSubpath(sourceFolder1, testFolder1))
        assertFalse(isSubpath(testFolder1, sourceFolder1))
        assertFalse(isSubpath(sourceFolder2, testFolder2))
        assertFalse(isSubpath(testFolder2, sourceFolder2))
    }

    @Test
    fun containsSubpath() {
        assertTrue(isSubpath(sourceFolder1, sourceFolder2))
        assertTrue(isSubpath(sourceFolder2, sourceFolder1))
        assertTrue(isSubpath(testFolder1, testFolder2))
        assertTrue(isSubpath(testFolder2, testFolder1))
    }

    @Test
    fun testMakeRootDir() {
        assertTrue(makeRootFolders())
        assertTrue(File(TEMP_FOLDER).exists())
        assertTrue(File(DATA_FOLDER).exists())
        assertTrue(File(REPORT_FOLDER).exists())
    }
}