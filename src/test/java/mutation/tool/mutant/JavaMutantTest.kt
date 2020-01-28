package mutation.tool.mutant

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.operator.FILE1
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.genJavaMutants
import mutation.tool.project.Project
import mutation.tool.util.MutationToolConfig
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class JavaMutantTest {

    @Test
    fun testGenerateMutants() {
        val contexts = getListOfAnnotationContext(File(FILE1), JavaStrategy())

        val config = MutationToolConfig(File(""), File(""))
        config.operators += listOf(OperatorsEnum.RMA, OperatorsEnum.RMAT)
        config.mutantsFolder = "./src/test/resources/mutants2/";

        resetMutantFoldersNum()
        genJavaMutants(contexts, File(FILE1), config, Project("fakeProject",
                File("./src/test/resources/fakeProject")))

        assertEquals(3, File("./src/test/resources/mutants2/").listFiles().size)
    }
}