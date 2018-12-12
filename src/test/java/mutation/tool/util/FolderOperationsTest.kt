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
        assertFalse(isSubFolder(sourceFolder1, testFolder1))
        assertFalse(isSubFolder(testFolder1, sourceFolder1))
        assertFalse(isSubFolder(sourceFolder2, testFolder2))
        assertFalse(isSubFolder(testFolder2, sourceFolder2))
    }

    @Test
    fun containsSubpath() {
        assertTrue(isSubFolder(sourceFolder1, sourceFolder2))
        assertTrue(isSubFolder(sourceFolder2, sourceFolder1))
        assertTrue(isSubFolder(testFolder1, testFolder2))
        assertTrue(isSubFolder(testFolder2, testFolder1))
    }

    @Test
    fun testMakeRootDir() {
        assertTrue(makeRootFolders())
        assertTrue(File(TEMP_FOLDER).exists())
        assertTrue(File(DATA_FOLDER).exists())
        assertTrue(File(REPORT_FOLDER).exists())
    }

    @Test
    fun testDeleteTempFolder() {
        makeRootFolders()
        if(File(TEMP_FOLDER).list().isEmpty())
            File(TEMP_FOLDER + File.separator + "tempFile").createNewFile()

        assertTrue(deleteTempFolder())
        assertFalse(File(TEMP_FOLDER).exists())

        makeRootFolders()
        assertTrue(deleteTempFolder())
        assertFalse(File(TEMP_FOLDER).exists())
    }
}