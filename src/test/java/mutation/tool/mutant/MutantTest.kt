package mutation.tool.mutant

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.operator.FILE1
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.getValidOperators
import mutation.tool.project.Project
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class MutantTest {

    @Test
    fun generateMutantsTest() {
        val contexts = getListOfAnnotationContext(File(FILE1))
        val operatorsEnum = listOf<OperatorsEnum>(OperatorsEnum.RMA, OperatorsEnum.RMAT)
        val validOperators = getValidOperators(contexts, File(FILE1), operatorsEnum)

        generateMutants(validOperators, File(FILE1), Project("fakeProject",
                File("/home/jcarlos_research/Documents/git/research/mutation-tool-for-annotations/src/test/" +
                        "resources/fakeProject")), File("./src/test/resources/mutants"))

        assertEquals(21, File("./src/test/resources/mutants/").listFiles().size)
    }
}