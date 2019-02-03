package mutation.tool.operator.ada

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.operator.FILE1
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class ADACheckerTest {

    private val jsonFile = File("./src/test/resources/configFiles/ADA_contexts.json")

    @Test
    fun testCheck() {
        val checker = ADAChecker(jsonFile)
        val contexts = getListOfAnnotationContext(File(FILE1))
        val operators = checker.check(contexts, File(FILE1))

        assertEquals(0, operators.size)
    }
}