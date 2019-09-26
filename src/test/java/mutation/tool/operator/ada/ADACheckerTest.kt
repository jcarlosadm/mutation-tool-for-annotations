package mutation.tool.operator.ada

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class ADACheckerTest {

    private val jsonFile = File("./src/test/resources/configFiles/annotations.json")

    @Test
    fun testCheck() {
        val annotationInfos = getAnnotationInfos(jsonFile)
        val checker = ADAChecker(annotationInfos)
        checker.build()

        val contexts = getListOfAnnotationContext(File(FILE1), JavaStrategy())
        val operators = checker.check(contexts, File(FILE1))

        assertEquals(22, operators.size)
    }
}