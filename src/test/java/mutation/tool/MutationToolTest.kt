package mutation.tool

import mutation.tool.mutant.resetMutantFoldersNum
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.Language
import mutation.tool.util.MutationToolConfig
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

private const val sourcePath = "./src/test/resources/fakeProject/src/main/java"
private const val sourcePath2 = "./src/test/resources/fakeProjectCSharp/src"
private const val testPath = "./src/test/resources/fakeProject/src/test/java"

internal class MutationToolTest {

    @Test
    fun testRunPathSourceNotExists() {
        assertTrue(File(testPath).exists())
        val config = MutationToolConfig(File(""), File(testPath))
        config.testOriginalProject = true
        assertFalse(MutationTool(config).run())
    }

    @Test
    fun testRunPathTestNotExists() {
        assertTrue(File(sourcePath).exists())
        val config = MutationToolConfig(File(sourcePath), File(""))
        config.testOriginalProject = true
        assertFalse(MutationTool(config).run())
    }

    @Test
    fun testMutantToolJava() {
        val config = MutationToolConfig(File(sourcePath))
        config.projectName = "fakeProject"
        config.operators.addAll(OperatorsEnum.values())
        config.mutantsFolder = "./src/test/resources/mutants"
        config.threads = 2
        config.setDebugOn()
        resetMutantFoldersNum()
        assertTrue(MutationTool(config).run())
    }

    @Test
    fun testMutantToolCSharp() {
        val config = MutationToolConfig(File(sourcePath2))
        config.pathAnnotationInfo = "./src/test/resources/configFiles/annotations-csharp.json"
        config.projectName = "fakeProject2"
        config.operators.addAll(listOf(OperatorsEnum.ADAT))
        config.language = Language.C_SHARP
        config.mutantsFolder = "./src/test/resources/mutants"
        config.threads = 2
        config.setDebugOn()
        resetMutantFoldersNum()
        assertTrue(MutationTool(config).run())
    }

}