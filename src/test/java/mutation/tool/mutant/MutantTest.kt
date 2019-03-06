package mutation.tool.mutant

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.operator.FILE1
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.getValidOperators
import mutation.tool.project.Project
import mutation.tool.util.MutationToolConfig
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class MutantTest {

    @Test
    fun testGenerateMutants() {
        val contexts = getListOfAnnotationContext(File(FILE1))

        val config = MutationToolConfig(File(""), File(""))
        config.operators += listOf(OperatorsEnum.RMA, OperatorsEnum.RMAT)

        val validOperators = getValidOperators(contexts, File(FILE1), config)

        resetMutantFoldersNum()
        generateMutants(validOperators, File(FILE1), Project("fakeProject",
                File("./src/test/resources/fakeProject")), File("./src/test/resources/mutants2"))

        assertEquals(3, File("./src/test/resources/mutants2/").listFiles().size)
    }
}