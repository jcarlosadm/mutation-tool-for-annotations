package mutation.tool.operator

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.util.MutationToolConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

const val FILE1 = "./src/test/resources/fakeProject/src/main/java/TarefasController.java"

internal class JavaOperatorTest {

    @Test
    fun testGetValidOperators() {
        val config = MutationToolConfig(File(""), File(""))
        config.operators += listOf(OperatorsEnum.RMA, OperatorsEnum.RMAT)
        val validOperators = getValidJavaOperators(getListOfAnnotationContext(File(FILE1), JavaStrategy()), File(FILE1), config)

        assertEquals(19, validOperators.size)
    }
}