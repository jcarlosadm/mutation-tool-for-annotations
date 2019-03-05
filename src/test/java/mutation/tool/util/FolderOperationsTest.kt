package mutation.tool.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private val sourceFolder1 = File("./src/test/resources/fakeProject/src/main/java")
private val sourceFolder2 = File("./src/test/resources/fakeProject/src/main")
private val testFolder1 = File("./src/test/resources/fakeProject/src/test/java")
private val testFolder2 = File("./src/test/resources/fakeProject/src/test")

private const val PATH = "./src/test/resources/fakeProject/src"
private const val FILENAME1 = "Main.java"
private const val FILENAME2 = "MainTest.java"
private const val FILENAME3 = "TarefasController.java"
private const val FILENAME4 = "TarefasController2.java"

internal class FolderOperationsTest {

    @Test
    fun testNotContainsSubpath() {
        assertFalse(isSubFolder(sourceFolder1, testFolder1))
        assertFalse(isSubFolder(testFolder1, sourceFolder1))
        assertFalse(isSubFolder(sourceFolder2, testFolder2))
        assertFalse(isSubFolder(testFolder2, sourceFolder2))
    }

    @Test
    fun testContainsSubpath() {
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

    @Test
    fun testGetAllJavaFiles() {
        val files = getAllJavaFiles(File(PATH))
        assertFalse(files.isEmpty())
        assertTrue(files.size == 4)
        for (file in files) {
            assertTrue(file.name == FILENAME1 || file.name == FILENAME2 || file.name == FILENAME3 ||
                    file.name == FILENAME4)
        }
    }
}